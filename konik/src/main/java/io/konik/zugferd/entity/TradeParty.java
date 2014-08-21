/*
 * Copyright (C) 2014 konik.io
 *
 * This file is part of Konik library.
 *
 * Konik library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Konik library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Konik library.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.konik.zugferd.entity;

import io.konik.zugferd.unqualified.ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * = The Trade Party
 * 
 * Applies to a buyer, seller, order recipient or invoice recipient.
 */
@XmlType(name = "TradePartyType", propOrder = { "id", "globalId", "name", "contact", "address", "taxRegistrations" })
public class TradeParty {

   /** The vendor number or customer number */
   @XmlElement(name = "ID")
   @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
   private String id;

   /** The global vendor number or customer number id */
   @XmlElement(name = "GlobalID")
   private List<ID> globalId;

   /** The name of the trade party. */
   @NotNull
   @XmlElement(name = "Name")
   private String name;

   /** The contact person. */
   @Valid
   @XmlElement(name = "DefinedTradeContact")
   private Contact contact;

   /** The postal address. */
   @Valid
   @XmlElement(name = "PostalTradeAddress")
   private Address address;

   /** The tax registration. */
   @Valid
   @XmlElement(name = "SpecifiedTaxRegistration")
   private List<TaxRegistration> taxRegistrations;

   /**
    * Gets the id.
    * 
    * Profile:: COMFORT when part of Trade.agreements.seller
    * 
    * Example:: {@code The supplier number given by the customer/buyer }
    * 
    * @return the id
    */
   public String getId() {
      return id;
   }

   /**
    * Sets the id.
    * Profile:: COMFORT when part of Trade.agreements.seller
    * 
    * Example:: {@code The supplier number given by the customer/buyer }
    *
    * @param id the new id
    * @return the trade party
    */
   public TradeParty setId(String id) {
      this.id = id;
      return this;
   }

   /**
    * Gets the trade party global id. (GLN, DUNS, BIC, ODETTE)
    * 
    * Profile:: COMFORT when part of Trade.agreements.seller
    * 
    * Example::
    * - {@link ID#getValue()} {@code  GENODED1SPK, 4000001000005 } - {@link ID#getSchemeId()} the ISO 6523 code
    * {@code 0021, 0088, 0060, 0177 }
    * 
    * @return the global id
    */
   public List<ID> getGlobalId() {
      if (globalId == null) {
         globalId = new ArrayList<ID>();
      }
      return this.globalId;
   }

   /**
    * Adds the global id.
    * 
    * Profile:: COMFORT when part of Trade.agreements.seller
    * 
    * Example::
    * - {@link ID#getValue()} {@code  GENODED1SPK, 4000001000005 } - {@link ID#getSchemeId()} the ISO 6523 code
    * {@code 0021, 0088, 0060, 0177 }
    * 
    * @param additionalGlobalId the additional global id
    * @return the trade party
    */
   public TradeParty addGlobalId(ID additionalGlobalId) {
      getGlobalId().add(additionalGlobalId);
      return this;
   }

   /**
    * Gets the trade party name. Usually the Company name.
    * 
    * Profile:: BASIC when part of Trade.agreements.seller/buyer.
    * 
    * Example:: {@code  ACME Inc.}
    * 
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * Sets the trade party name. Usually the Company name.
    * 
    * Profile:: BASIC when part of Trade.agreements.seller/buyer.
    * 
    * Example:: {@code  ACME Inc.}
    * 
    * @param name the name
    * @return the trade party
    */
   public TradeParty setName(String name) {
      this.name = name;
      return this;
   }

   /**
    * Gets the contact person.
    * 
    * Profile:: BASIC when part of Trade.agreements.seller/buyer.
    * 
    * 
    * @return the defined trade contact
    */
   public Contact getContact() {
      return contact;
   }

   /**
    * Sets the contact person.
    * 
    * Profile:: BASIC when part of Trade.agreements.seller/buyer.
    * 
    * 
    * @param contact the new defined trade contact
    * @return the trade party
    */
   public TradeParty setContact(Contact contact) {
      this.contact = contact;
      return this;
   }

   /**
    * Gets the postal trade address.
    * 
    * Profile:: BASIC when part of Trade.agreements.seller/buyer.
    * 
    * 
    * @return the postal trade address
    */
   public Address getAddress() {
      return address;
   }

   /**
    * Sets the postal trade address.
    * 
    * Profile:: BASIC when part of Trade.agreements.seller/buyer.
    * 
    * 
    * @param postalAddress the new postal trade address
    * @return the trade party
    */
   public TradeParty setAddress(Address postalAddress) {
      this.address = postalAddress;
      return this;
   }

   /**
    * Gets the specified tax registration.
    * 
    * Profile:: BASIC
    * 
    * 
    * @return the specified tax registration
    */
   public List<TaxRegistration> getTaxRegistrations() {
      if (taxRegistrations == null) {
         taxRegistrations = new ArrayList<TaxRegistration>();
      }
      return this.taxRegistrations;
   }

   /**
    * Adds the tax registration.
    * 
    * Profile:: BASIC
    * 
    * 
    * @param additionalTaxRegistration an additional Tax Registration
    * @return the trade party
    */
   public TradeParty addTaxRegistrations(TaxRegistration... additionalTaxRegistration) {
      Collections.addAll(getTaxRegistrations(), additionalTaxRegistration);
      return this;
   }

}
