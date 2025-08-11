package com.java.healSync.service.serviceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import com.java.healSync.dto.AuthRequestDto;
import com.java.healSync.dto.AuthResponseDto;
import com.java.healSync.exception.InvalidCredentialsException;
import com.java.healSync.exception.InvalidRoleException;
import com.java.healSync.service.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.healSync.service.services.UserService;
import com.java.healSync.repository.AppointmentRepository;
import com.java.healSync.repository.DoctorRepository;
import com.java.healSync.repository.PatientRepository;
import com.java.healSync.repository.SpecializationRepository;
import com.java.healSync.repository.UserRepository;
import com.java.healSync.entity.Appointment;
import com.java.healSync.entity.Doctor;
import com.java.healSync.entity.Patient;
import com.java.healSync.enums.Role;
import com.java.healSync.entity.Specialization;
import com.java.healSync.entity.User;
import com.java.healSync.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private AppointmentRepository appointmentDao;

	@Autowired
	private SpecializationRepository specializationRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User save(UserDto userDto) {
		// If the role is ADMIN, check if there is another admin
		if (userDto.getRole() == Role.ADMIN) {
			boolean adminExists = userRepository.existsByRole(Role.ADMIN);
			if (adminExists) {
				throw new IllegalStateException("An admin already exists in the system. A new admin record cannot be created.");
			}
		}

		// Creating a new user object
		User user = new User(userDto.getEmail(), userDto.getPassword(), userDto.getRole(), userDto.getGender(), userDto.getFirstName(), userDto.getLastName());
//		user.setPassword(bCryptPasswordEncoder
//				.encode(user.getPassword())); // Encrypting password before saving
		user = userRepository.save(user);

		// Saving to the relevant table based on the role
		if (userDto.getRole() == Role.PATIENT) {
			Patient patient = new Patient();
			patient.setUser(user);  // Associate Patient with User
			patientRepository.save(patient); // Save to the Patient table

		} else if (userDto.getRole() == Role.DOCTOR) {
			Doctor doctor = new Doctor();
			doctor.setUser(user);

			if (userDto.getSpecializationId() != null) { // If such a specialization exists
				Specialization specialization = specializationRepository.findById(userDto.getSpecializationId())
						.orElseThrow(() -> new IllegalArgumentException("Specialization not found"));
				doctor.setSpecialization(specialization); // Set this specialization info to the doctor object
			}
			doctorRepository.save(doctor); // Save the doctor object with specialization info in the database
		}

		return user;
	}


	public AuthResponseDto authenticateAndGetToken(AuthRequestDto authRequestDto) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword())
			);
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException("Invalid credentials. Please check email/password.");
		} catch (AuthenticationException e) {
			throw new InvalidCredentialsException("Authentication failed. Please try again.");
		}

		User user = userRepository.findByEmail(authRequestDto.getEmail());

		if (user.getRole() == null) {
			throw new InvalidRoleException("Role(null) is not valid for this account.");
		}

		String token = jwtService.generateToken(authRequestDto.getEmail());
		System.out.println("&&&token:-"+token);
		return new AuthResponseDto(token, user.getRole(), user.getId(), user.getFirstName(), user.getLastName());
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public void deleteUser(Long userId) {
		// Find the user in the database
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));

		// Check associated appointments based on user role
		if (user.getRole() == Role.PATIENT) {
			Patient patient = patientRepository.findByUser(user).orElseThrow(() -> new IllegalStateException("Patient not found"));

			if (patient != null) {
				// Delete appointments associated with the patient
				List<Appointment> appointments = appointmentDao.findByPatient(patient);
				for (Appointment appointment : appointments) {
					appointmentDao.delete(appointment);
				}
				patientRepository.delete(patient);  // Delete Patient
			}
		} else if (user.getRole() == Role.DOCTOR) {
			Doctor doctor = doctorRepository.findByUser(user).orElseThrow(() -> new IllegalStateException("Doctor not found"));

			if (doctor != null) {
				// Delete appointments associated with the doctor
				List<Appointment> appointments = appointmentDao.findByDoctor(doctor);
				for (Appointment appointment : appointments) {
					appointmentDao.delete(appointment);
				}
				doctorRepository.delete(doctor);  // Delete Doctor
			}
		}

		// Finally, delete the User
		userRepository.delete(user);
	}

	@Override
	public boolean updateUser(Long id, User user) {
		if (userRepository.existsById(id)) {
			User existingUser = userRepository.findById(id).orElseThrow();
			existingUser.setFirstName(user.getFirstName());
			existingUser.setLastName(user.getLastName());
			existingUser.setEmail(user.getEmail());
			existingUser.setPassword(user.getPassword());
			existingUser.setRole(user.getRole());
			existingUser.setGender(user.getGender());
			userRepository.save(existingUser);
			return true;
		}
		return false;
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Map<String, Object> getUserIdByCredentials(String email, String password, Role role) {
		Optional<User> user = userRepository.findByEmailAndPasswordAndRole(email, password, role);

		if (user.isPresent()) {
			Map<String, Object> response = new HashMap<>();
			response.put("userId", user.get().getId());  // Return User ID

			response.put("firstName", user.get().getFirstName());
			response.put("lastName", user.get().getLastName());

			if (role == Role.PATIENT) {
				// If Patient, add Patient ID
				Optional<Patient> patient = patientRepository.findByUser(user.get());
				patient.ifPresent(p -> response.put("patientId", p.getId()));
			} else if (role == Role.DOCTOR) {
				// If Doctor, add Doctor ID
				Optional<Doctor> doctor = doctorRepository.findByUser(user.get());
				doctor.ifPresent(d -> response.put("doctorId", d.getId()));
			}

			return response;
		}

		return null;  // Return null if user is not found
	}
}