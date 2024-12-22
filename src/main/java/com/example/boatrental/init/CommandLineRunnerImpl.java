package com.example.boatrental.init;

import com.example.boatrental.dtos.*;
import com.example.boatrental.models.enums.*;
import com.example.boatrental.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private BoatService boatService;
    @Autowired
    private BookingService bookingService;

    public CommandLineRunnerImpl(UserService userService, BoatService boatService, BookingService bookingService) {
        this.userService = userService;
        this.boatService = boatService;
        this.bookingService = bookingService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
    }

    private void seedData() throws IOException, InterruptedException {

        UserDto u1 = new UserDto("Мешкова Александра Станиславовна", "user1@register", "2(34)504-56-72", "userService", Role.ADMIN, LocalDate.now());
        UserDto u2 = new UserDto("Ермолаева Маргарита Ильинична", "user2@register", "60(298)988-12-32", "Exception504", Role.CLIENT, LocalDate.now());
        UserDto u3 = new UserDto("Герасимова Валерия Артёмовна", "user3@register", "71(02)207-30-95", "207207207207", Role.WORKER, LocalDate.now());
        UserDto u4 = new UserDto("Кузнецова Анна Петровна", "user4@register", "80(123)456-78-90", "userService4", Role.WORKER, LocalDate.now());
        UserDto u5 = new UserDto("Сидоров Иван Васильевич", "user5@register", "90(987)654-32-10", "userService5", Role.CLIENT, LocalDate.now());
        userService.register(u1);
        userService.register(u2);
        userService.register(u3);
        userService.register(u4);
        userService.register(u5);

        BoatDto b1 = new BoatDto("Моторная лодка FD-SDF", "Удобная моторная лодка для рыбалки", BoatType.MOTORBOAT, 4, BoatStatus.AVAILABLE, 1500.00, LocalDate.now(), LocalDate.now().minusMonths(12));
        BoatDto b2 = new BoatDto("Парусная лодка JS-0110", "Элегантная парусная лодка для прогулок", BoatType.SAILBOAT, 6, BoatStatus.AVAILABLE, 2000.00, LocalDate.now(), LocalDate.now().minusMonths(4));
        BoatDto b3 = new BoatDto("Катер GD-4123", "Мощный катер для скоростных поездок", BoatType.MOTORBOAT, 8, BoatStatus.AVAILABLE, 25000.00, LocalDate.now(), LocalDate.now().minusMonths(8));
        boatService.register(b1);
        boatService.register(b2);
        boatService.register(b3);

        createBooking(u1.getEmail(), b1.getName(), 2);
        createBooking(u2.getEmail(), b2.getName(), 3);
        createBooking(u3.getEmail(), b3.getName(), 4);
//        createBooking(u4.getEmail(), b1.getName(), 5);
//        createBooking(u5.getEmail(), b2.getName(), 6);

    }

    private void createBooking(String userEmail, String boatName, int hours) {
        Optional<UUID> userIdOpt = userService.getUserIdByEmail(userEmail);
        Optional<UUID> boatIdOpt = boatService.getBoatIdByName(boatName);

        if (userIdOpt.isPresent() && boatIdOpt.isPresent()) {
            UUID userId = userIdOpt.get();
            UUID boatId = boatIdOpt.get();

            BookingDto bookingDto = new BookingDto(
                    userId,
                    boatId,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(hours),
                    BookingStatus.CONFIRMED,
                    BigDecimal.valueOf(0),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            bookingService.createBooking(bookingDto);
        } else {
            System.out.println("Не удалось найти пользователя или лодку для создания бронирования.");
        }
    }
}
