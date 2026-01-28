    package com.booking.application;

    import com.booking.application.entity.User;
    import com.booking.application.enums.Role;
    import com.booking.application.repository.UserRepository;
    import org.springframework.boot.CommandLineRunner;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.annotation.Bean;
    import org.springframework.security.crypto.password.PasswordEncoder;

    @SpringBootApplication
    public class BookingApplication {
        public static void main(String[] args) {
            SpringApplication.run(BookingApplication.class, args);
        }
        @Bean
        CommandLineRunner initAdmin(UserRepository repo, PasswordEncoder encoder) {
            return args -> {
                if (!repo.existsByEmail("admin@gmail.com")) {
                    User admin = new User();
                    admin.setName("Admin User");
                    admin.setEmail("admin@gmail.com");
                    admin.setPassword(encoder.encode("Admin@123"));
                    admin.setRole(Role.ROLE_ADMIN);
                    repo.save(admin);
//                    System.out.println(" ADMIN CREATED: admin@gmail.com / Admin@123");

                }
            };
        }


    }
