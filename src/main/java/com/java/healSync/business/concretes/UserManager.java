package com.java.healSync.business.concretes;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.healSync.business.abstracts.UserService;
import com.java.healSync.dataAccess.AppointmentDao;
import com.java.healSync.dataAccess.DoctorDao;
import com.java.healSync.dataAccess.PatientDao;
import com.java.healSync.dataAccess.SpecializationDao;
import com.java.healSync.dataAccess.UserDao;
import com.java.healSync.entities.Appointment;
import com.java.healSync.entities.Doctor;
import com.java.healSync.entities.Patient;
import com.java.healSync.entities.Role;
import com.java.healSync.entities.Specialization;
import com.java.healSync.entities.User;
import com.java.healSync.entities.UserDto;

@Service
public class UserManager implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private DoctorDao doctorDao;

	@Autowired
	private PatientDao patientDao;

	@Autowired
	private AppointmentDao appointmentDao;

	@Autowired
	private SpecializationDao specializationDao;

	@Override
	public User save(UserDto userDto) {
		// If the role is ADMIN, check if there is another admin
		if (userDto.getRole() == Role.ADMIN) {
			boolean adminExists = userDao.existsByRole(Role.ADMIN);
			if (adminExists) {
				throw new IllegalStateException("An admin already exists in the system. A new admin record cannot be created.");
			}
		}

		// Creating a new user object
		User user = new User(userDto.getEmail(), userDto.getPassword(), userDto.getRole(), userDto.getGender(), userDto.getFirstName(), userDto.getLastName());

		user = userDao.save(user);

		// Also save to the relevant table based on the role
		if (userDto.getRole() == Role.PATIENT) {
			Patient patient = new Patient();
			patient.setUser(user);  // Associate Patient with User
			patientDao.save(patient); // Save to the Patient table

		} else if (userDto.getRole() == Role.DOCTOR) {
			Doctor doctor = new Doctor();
			doctor.setUser(user);

			if (userDto.getSpecializationId() != null) { // If such a specialization exists
				Specialization specialization = specializationDao.findById(userDto.getSpecializationId())
						.orElseThrow(() -> new IllegalArgumentException("Specialization not found"));
				doctor.setSpecialization(specialization); // Set this specialization info to the doctor object
			}
			doctorDao.save(doctor); // Save the doctor object with specialization info in the database
		}

		return user;
	}

	@Override
	public boolean authenticate(String email, String password, Role role) {
		User user = userDao.findByEmail(email);
		return user != null && user.getPassword().equals(password) && user.getRole() == role;
	}

	@Override
	public List<User> getAllUsers() {
		return userDao.findAll();
	}

	@Override
	public void deleteUser(Long userId) {
		// Find the user in the database
		User user = userDao.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));

		// Check associated appointments based on user role
		if (user.getRole() == Role.PATIENT) {
			Patient patient = patientDao.findByUser(user).orElseThrow(() -> new IllegalStateException("Patient not found"));

			if (patient != null) {
				// Delete appointments associated with the patient
				List<Appointment> appointments = appointmentDao.findByPatient(patient);
				for (Appointment appointment : appointments) {
					appointmentDao.delete(appointment);
				}
				patientDao.delete(patient);  // Delete Patient
			}
		} else if (user.getRole() == Role.DOCTOR) {
			Doctor doctor = doctorDao.findByUser(user).orElseThrow(() -> new IllegalStateException("Doctor not found"));

			if (doctor != null) {
				// Delete appointments associated with the doctor
				List<Appointment> appointments = appointmentDao.findByDoctor(doctor);
				for (Appointment appointment : appointments) {
					appointmentDao.delete(appointment);
				}
				doctorDao.delete(doctor);  // Delete Doctor
			}
		}

		// Finally, delete the User
		userDao.delete(user);
	}

	@Override
	public boolean updateUser(Long id, User user) {
		if (userDao.existsById(id)) {
			User existingUser = userDao.findById(id).orElseThrow();
			existingUser.setFirstName(user.getFirstName());
			existingUser.setLastName(user.getLastName());
			existingUser.setEmail(user.getEmail());
			existingUser.setPassword(user.getPassword());
			existingUser.setRole(user.getRole());
			existingUser.setGender(user.getGender());
			userDao.save(existingUser);
			return true;
		}
		return false;
	}

	@Override
	public User getUserByEmail(String email) {
		return userDao.findByEmail(email);
	}

	public Map<String, Object> getUserIdByCredentials(String email, String password, Role role) {
		Optional<User> user = userDao.findByEmailAndPasswordAndRole(email, password, role);

		if (user.isPresent()) {
			Map<String, Object> response = new HashMap<>();
			response.put("userId", user.get().getId());  // Return User ID

			response.put("firstName", user.get().getFirstName());
			response.put("lastName", user.get().getLastName());

			if (role == Role.PATIENT) {
				// If Patient, add Patient ID
				Optional<Patient> patient = patientDao.findByUser(user.get());
				patient.ifPresent(p -> response.put("patientId", p.getId()));
			} else if (role == Role.DOCTOR) {
				// If Doctor, add Doctor ID
				Optional<Doctor> doctor = doctorDao.findByUser(user.get());
				doctor.ifPresent(d -> response.put("doctorId", d.getId()));
			}

			return response;
		}

		return null;  // Return null if user is not found
	}
}