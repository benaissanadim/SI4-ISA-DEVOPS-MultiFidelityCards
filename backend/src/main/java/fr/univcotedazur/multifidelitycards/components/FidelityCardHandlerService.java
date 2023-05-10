package fr.univcotedazur.multifidelitycards.components;

import fr.univcotedazur.multifidelitycards.entities.Client;
import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.entities.GeographicZone;
import fr.univcotedazur.multifidelitycards.exceptions.*;
import fr.univcotedazur.multifidelitycards.interfaces.*;
import fr.univcotedazur.multifidelitycards.repositories.FidelityCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * FidelityCardHandlerService allows to create a new fidelity card and to pay
 */
@Transactional
@Component
public class FidelityCardHandlerService implements  FidelityCardCreator, FidelityCardFinder, IFidelityCardHander {

    Logger log = LoggerFactory.getLogger(FidelityCardHandlerService.class);

    private final ClientFinder clientFinder;
    private final FidelityCardRepository fidelityCardRepository;
    private final GeographicZoneFinder geographicZoneFinder;
    private final IStoreFinder storeFinder;
    private static final double FIDELITY_RATE = 0.1;

    public FidelityCard create(Long clientID, String zoneName) throws ClientNotFoundException, AlreadyExistingFidelityCardException, GeographicZoneNotFoundException {
        log.debug("creating fidelity card for clientId {} in zone {}", clientID, zoneName);
        Client client = this.clientFinder.findById(clientID);
        Optional<FidelityCard> optFidelityCard = this.fidelityCardRepository.findFidelityCardByClient_Id(clientID);
        if (optFidelityCard.isPresent()) {
            log.warn("fidelity card with client id {} already exists", clientID);
            throw new AlreadyExistingFidelityCardException(clientID.toString());
        }
        GeographicZone zone = this.geographicZoneFinder.findByName(zoneName);
        FidelityCard fidelityCard = new FidelityCard(client,zone);
        this.fidelityCardRepository.save(fidelityCard);
        log.info("fidelity card created created with SUCCESS");
        return fidelityCard;
    }

    @Autowired
    public FidelityCardHandlerService(ClientFinder clientFinder, FidelityCardRepository fidelityCardRepository,
            GeographicZoneFinder geographicZoneFinder, IStoreFinder storeFinder) {
        this.clientFinder = clientFinder;
        this.fidelityCardRepository = fidelityCardRepository;
        this.geographicZoneFinder = geographicZoneFinder;
        this.storeFinder = storeFinder;
    }

    public FidelityCard retrievePoints(FidelityCard fidelityCard, int nbPoints) throws NotEnoughPointsException {
        if(fidelityCard.getPoints() < nbPoints){
            throw new NotEnoughPointsException("not enough points to retrieve");
        }
        fidelityCard.setPoints(fidelityCard.getPoints() - nbPoints);
        this.fidelityCardRepository.save(fidelityCard);
        return fidelityCard;
    }

    public FidelityCard addPointsFromAmount(FidelityCard fidelityCard, double amount) {
        int points = (int)(amount * FIDELITY_RATE);
        fidelityCard.setPoints(fidelityCard.getPoints() + points);
        this.fidelityCardRepository.save(fidelityCard);
        return fidelityCard;
    }

    public FidelityCard addAmount(FidelityCard fidelityCard, double amount) {
        log.info("add amount {} to fidelity card {}", amount, fidelityCard.getNumber());
        fidelityCard.setAmount(fidelityCard.getAmount() + amount);
        this.fidelityCardRepository.save(fidelityCard);
        return fidelityCard;
    }

    public FidelityCard retreiveAmount(FidelityCard fidelityCard, double amount) throws InvalidPaymentException {
        if (fidelityCard.getAmount() < amount) {
            log.warn("not enough money to buy the product");
            throw new InvalidPaymentException("not enough money to buy the product");
        }
        fidelityCard.setAmount(fidelityCard.getAmount() - amount);
        this.fidelityCardRepository.save(fidelityCard);
        return fidelityCard;
    }

    @Override public FidelityCard findById(Long fidelityCardId) throws FidelityCardNotFoundException {
        Optional<FidelityCard> optFidelityCard = fidelityCardRepository.findById(fidelityCardId);
        if (optFidelityCard.isEmpty()) {
            String msg = "fidelity card with id " + fidelityCardId + " not found";
            log.warn(msg);
            throw new FidelityCardNotFoundException(msg);
        }
        return optFidelityCard.get();
    }

    @Override
    public List<FidelityCard> findAll() {
        return fidelityCardRepository.findAll();
    }
}
