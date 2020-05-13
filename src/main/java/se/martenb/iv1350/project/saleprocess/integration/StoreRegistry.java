package se.martenb.iv1350.project.saleprocess.integration;

import se.martenb.iv1350.project.saleprocess.integration.dto.StoreDTO;
import se.martenb.iv1350.project.saleprocess.integration.dto.AddressDTO;

/**
 * Store registry handles communications with an external store information
 * registry.
 */
public class StoreRegistry {
    /**
     * Constructs a new instance for communications with the external system.
     */
    public StoreRegistry() {}
    
    /**
     * Dirty dummy for fetching store information from the external registry.
     * @return Information for the dummy store.
     */
    public StoreDTO getStoreInfo() {
        String dummyStreetName = "Baker Street";
        int dummyStreetNumber = 99;
        int dummyPostalCode = 31415;
        String dummyPostTown = "Gotham City";
        String dummyStoreName = "The Leftorium";
        
        AddressDTO dummyAddress = new AddressDTO(dummyStreetName, 
                dummyStreetNumber, dummyPostalCode, dummyPostTown);
        StoreDTO dummyStore = new StoreDTO(dummyStoreName, dummyAddress);
        return dummyStore;
    }
    
}
