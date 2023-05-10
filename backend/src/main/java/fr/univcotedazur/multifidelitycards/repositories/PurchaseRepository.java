package fr.univcotedazur.multifidelitycards.repositories;

import fr.univcotedazur.multifidelitycards.entities.FidelityCard;
import fr.univcotedazur.multifidelitycards.entities.PurchaseAction;
import fr.univcotedazur.multifidelitycards.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseAction, Long> {

    @Query("select distinct p from PurchaseAction p where p.fidelityCard = ?1 and p.store = ?2 "
            + "order by p.createdAt desc")
    List<PurchaseAction> findCardPurchasesInStore(FidelityCard card, Store store);

    @Query("select distinct p from PurchaseAction p where p.fidelityCard = ?1 and p.createdAt >= ?2 ")
    List<PurchaseAction> findPurchasesByFidelityCardAfter(FidelityCard card, LocalDateTime date);

    @Query("select distinct p from PurchaseAction p where p.store.id = ?1 "
            + "order by p.createdAt desc")
    List<PurchaseAction> findAllPurchasesInStore(Long storeId);


}
