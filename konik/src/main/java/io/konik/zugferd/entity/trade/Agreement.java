/* Copyright (C) 2014 konik.io
 *
 * This file is part of the Konik library.
 *
 * The Konik library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * The Konik library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the Konik library. If not, see <http://www.gnu.org/licenses/>.
 */
package io.konik.zugferd.entity.trade;

import io.konik.zugferd.entity.CommonAgreement;
import io.konik.zugferd.entity.ReferencedDocument;
import io.konik.zugferd.entity.TradeParty;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * = The Trade Agreement.
 * 
 */
public class Agreement extends CommonAgreement<ReferencedDocument>{
   
   /**
    * Gets the buyer reference.
    * 
    * The reference to ease the attribution for the buyer
    * 
    * Profile:: COMFORT
    * 
    * @return the buyer reference
    */
   public String getBuyerReference() {
      return buyerReference;
   }

   /**
    * Sets the buyer reference. 
    * 
    * The reference to ease the attribution for the buyer
    * 
    * Profile:: COMFORT
    *
    * @param buyerReference the new buyer reference
    * @return the trade agreement
    */
   public Agreement setBuyerReference(String buyerReference) {
      this.buyerReference = buyerReference;
      return this;
   }

   /**
    * Gets the seller trade party.
    *
    * @return the seller trade party
    */
   public TradeParty getSellerTradeParty() {
      return seller;
   }

   /**
    * Sets the seller trade party.
    *
    * @param seller the new seller trade party
    * @return the trade agreement
    */
   public Agreement setSellerTradeParty(TradeParty seller) {
      this.seller = seller;
      return this;
   }

   /**
    * Gets the buyer trade party.
    *
    * @return the buyer trade party
    */
   public TradeParty getBuyer() {
      return buyer;
   }

   /**
    * Sets the buyer trade party.
    *
    * @param buyer the new buyer trade party
    * @return the supply chain trade agreement
    */
   public Agreement setBuyer(TradeParty buyer) {
      this.buyer = buyer;
      return this;
   }
   
   /**
    * Gets the delivery terms.
    *
    * Profile:: EXTENDED
    *
    * @return the delivery terms
    */
   public String getDeliveryTerms() {
      return deliveryTerms;
   }

   /**
    * Sets the delivery terms.
    *
    * Profile:: EXTENDED
    *
    * @param deliveryTerms the delivery terms
    */
   public void setDeliveryTerms(String deliveryTerms) {
      this.deliveryTerms = deliveryTerms;
   }
   
   /**
    * Gets the buyer order referenced document.
    *
    * @return the buyer order referenced document
    */
   @Override
   public ReferencedDocument getBuyerOrder() {
      return buyerOrder;
   }


   /**
    * Sets the buyer order referenced document.
    *
    * @param buyerOrder the new buyer order referenced document
    * @return the supply chain trade agreement
    */
   @Override
   public Agreement setBuyerOrder(ReferencedDocument buyerOrder) {
      this.buyerOrder = buyerOrder;
      return this;
   }

   /**
    * Gets the contract referenced document.
    * 
    * Profile:: COMFORT 
    *
    * @return the contract referenced document
    */
   @Override
   public ReferencedDocument getContract() {
      return contract;
   }

   /**
    * Sets the contract referenced document.
    * 
    * Profile:: COMFORT 
    *
    * @param contract the new contract referenced document
    * @return the supply chain trade agreement
    */
   @Override
   public Agreement setContract(ReferencedDocument contract) {
      this.contract = contract;
      return this;
   }
   
   @Override
   public List<ReferencedDocument> getAdditional() {
      if (additional == null) {
         additional = new ArrayList<ReferencedDocument>();
      }
      return additional;
   }
   

   @Override
   public CommonAgreement<ReferencedDocument> addAdditional(ReferencedDocument additionalReference) {
      getAdditional().add(additionalReference);
      return this;
   }
   
   /**
    * Gets the customer order referenced document.
    * 
    * Profile:: COMFORT 
    *
    * @return the customer order referenced document
    */
   @Override
   public ReferencedDocument getCustomerOrder() {
      return customerOrder;
   }

   /**
    * Sets the customer order referenced document.
    *  
    * Profile:: COMFORT 
    *
    * @param customerOrder the new customer order referenced document
    * @return the supply chain trade agreement
    */
   @Override
   public Agreement setCustomerOrder(ReferencedDocument customerOrder) {
      this.customerOrder = customerOrder;
      return this;
   }
   
   
}
