package com.javastrong.reservationservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.persistence.Id;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import java.util.Collection;
import java.util.stream.Stream;

@SpringBootApplication
public class ReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }

}

//callback interface
@Component
class Initializer implements ApplicationRunner {

    private final ReservationRepository reservationRepository;

    Initializer(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run (ApplicationArguments applicationArguments) throws Exception {
        Stream.of("Ken", "Linden", "Rowan", "Lesley", "Willow", "Josh", "Paula", "Cliff")
                .forEach(name -> reservationRepository.save(new Reservation(null, name)));
        reservationRepository.findAll().forEach(System.out::println);
    }

}

@RestController
@RefreshScope
class MessageRestController {
    private final String message;

    MessageRestController(@Value("${message}") String message) {
        this.message = message;
        }

        @GetMapping ("/message")
        String read(){
        return this.message;
        }
    }


@RestController
class ReservationRestController {
    private final ReservationRepository reservationRepository;

    ReservationRestController (ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/reservations")
    Collection<Reservation> reservations () {
        return this.reservationRepository.findAll();
    }
}

interface ReservationRepository extends JpaRepository<Reservation, Long> {


    //Select * from reservations where reservation_name = rn;
    Collection<Reservation> findByReservationName (String rn);

}

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
class Reservation {

    @Id
    @GeneratedValue
    private Long id;
    private String reservationName;

}
