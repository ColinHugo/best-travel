package com.curso.best.travel;

import com.curso.best.travel.domain.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BestTravelApplication {

	private final IHotelRepository hotelRepository;
	private final IFlyRepository flyRepository;
	private final ITickerRepository tickerRepository;
	private final IReservationRepository reservationRepository;
	private final ITourRepository tourRepository;
	private final ICustomerRepository customerRepository;

	public BestTravelApplication(
			IHotelRepository hotelRepository,
			IFlyRepository flyRepository,
			ITickerRepository tickerRepository,
			IReservationRepository reservationRepository,
			ITourRepository tourRepository,
			ICustomerRepository customerRepository ) {

		this.hotelRepository = hotelRepository;
		this.flyRepository = flyRepository;
		this.tickerRepository = tickerRepository;
		this.reservationRepository = reservationRepository;
		this.tourRepository = tourRepository;
		this.customerRepository = customerRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(BestTravelApplication.class, args);
	}

	// @Override
	// public void run(String... args) throws Exception {

		/* var fly = flyRepository.findById( 15L ).get();
		var hotel = hotelRepository.findById( 7L ).get();
		var ticker = tickerRepository.findById( UUID.fromString( "12345678-1234-5678-2236-567812345678" ) ).get();
		var reservation = reservationRepository.findById( UUID.fromString( "12345678-1234-5678-1234-567812345678" ) ).get();
		var customer = customerRepository.findById( "BBMB771012HMCRR022" ).get();

		log.info( String.valueOf( fly ) );
		log.info( String.valueOf( hotel ) );
		log.info( String.valueOf( ticker ) );
		log.info( String.valueOf( reservation) );
		log.info( String.valueOf( customer ) ); */

		/* System.out.println( "selectLessPrice" );
		this.flyRepository.selectLessPrice( BigDecimal.valueOf( 20 ) ).forEach( System.out::println );

		System.out.println();

		System.out.println( "selectBetweenPrice" );
		this.flyRepository.selectBetweenPrice( BigDecimal.valueOf( 10 ), BigDecimal.valueOf( 15 ) ).forEach( System.out::println );

		System.out.println();

		System.out.println( "selectOriginDestiny" );
		this.flyRepository.selectOriginDestiny( "Grecia", "Mexico" ).forEach( System.out::println ); */

		/* var fly = flyRepository.findByTicketId( UUID.fromString( "42345678-1234-5678-5233-567812345678" ) ).get();
		System.out.println( fly ); */

		// hotelRepository.findByPriceLessThan( BigDecimal.valueOf( 100 ) ).forEach( System.out::println );

		// hotelRepository.findByPriceIsBetween( BigDecimal.valueOf( 100 ), BigDecimal.valueOf( 200 ) ).forEach( System.out::println );

		// hotelRepository.findByRatingGreaterThan( 3 ).forEach( System.out::println );

		/* var hotel = hotelRepository.findByReservationId( UUID.fromString( "52345678-1234-5678-1234-567812345678" ) );
		System.out.println("hotel = " + hotel); */

		/* var customer = customerRepository.findById( "GOTW771012HMRGR087" ).orElseThrow();
		log.info( "Client name: " + customer.getFullName() );

		var fly = flyRepository.findById( 11L ).orElseThrow();
		log.info( "Fly " + fly.getOriginName() + " - " + fly.getDestinyName() );

		var hotel = hotelRepository.findById( 3L ).orElseThrow();
		log.info( "Hotel " + hotel.getName() );


		var tour = TourEntity.builder()
				.customer( customer )
				.build();

		var ticket = TicketEntity.builder()
				.id( UUID.randomUUID() )
				.price( fly.getPrice().multiply( BigDecimal.TEN ) )
				.arrivalDate( LocalDateTime.now() )
				.departureDate( LocalDateTime.now() )
				.purchaseDate( LocalDate.now() )
				.customer( customer )
				.tour( tour )
				.fly( fly )
				.build();

		var reservation = ReservationEntity.builder()
				.id( UUID.randomUUID() )
				.dateTimeReservation( LocalDateTime.now() )
				.dateEnd( LocalDate.now().plusDays( 2 ) )
				.dateStart( LocalDate.now().plusDays( 1 ) )
				.hotel( hotel )
				.customer( customer )
				.tour( tour )
				.totalDays( 1 )
				.price( hotel.getPrice().multiply( BigDecimal.TEN ) )
				.build();

		System.out.println( "-----			SAVING			-----" );

		tour.addReservation( reservation );
		tour.updateReservation();

		tour.addTicket( ticket );
		tour.updateTickets();

		var tourSaved = this.tourRepository.save( tour );

		Thread.sleep( 8000 );

		this.tourRepository.deleteById( tourSaved.getId() );

	} */
}
