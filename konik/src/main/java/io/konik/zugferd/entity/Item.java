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

import io.konik.zugferd.entity.trade.item.SpecifiedAgreement;
import io.konik.zugferd.entity.trade.item.SpecifiedDelivery;
import io.konik.zugferd.entity.trade.item.SpecifiedSettlement;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * = The Trade Item.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SupplyChainTradeLineItemType", propOrder = { "position", "agreement", "delivery","settlement", "product" })
public class Item {

   @Valid
	@XmlElement(name = "AssociatedDocumentLineDocument")
	private DocumentLine position;

   @Valid
	@XmlElement(name = "SpecifiedSupplyChainTradeAgreement")
	private SpecifiedAgreement agreement;

   @Valid
	@XmlElement(name = "SpecifiedSupplyChainTradeDelivery")
	private SpecifiedDelivery delivery;

   @Valid
	@XmlElement(name = "SpecifiedSupplyChainTradeSettlement")
	private SpecifiedSettlement settlement;

   @Valid
	@XmlElement(name = "SpecifiedTradeProduct")
	private Product product;

   /**
    * Gets the item position document.
    *
    * @return the position
    */
	public DocumentLine getPosition() {
		return position;
	}

	/**
    * Sets the item position document.
    *
    * @param position the new position
    * @return the item
    */
	public Item setPosition(DocumentLine position) {
		this.position = position;
		return this;
	}

	/**
	 * Gets the specified supply chain trade agreement.
	 *
	 * @return the specified supply chain trade agreement
	 */
	public SpecifiedAgreement getAgreement() {
		return agreement;
	}

	/**
	 * Sets the specified supply chain trade agreement.
	 *
	 * @param agreement the new specified supply chain trade agreement
	 * @return the item
	 */
	public Item setAgreement(SpecifiedAgreement agreement) {
		this.agreement = agreement;
		return this;
	}

	/**
	 * Gets the specified supply chain trade delivery.
	 *
	 * @return the specified supply chain trade delivery
	 */
	public SpecifiedDelivery getDelivery() {
		return delivery;
	}

	/**
	 * Sets the specified supply chain trade delivery.
	 *
	 * @param delivery the new specified supply chain trade delivery
	 * @return the item
	 */
	public Item setDelivery(SpecifiedDelivery delivery) {
		this.delivery = delivery;
		return this;
	}

	/**
	 * Gets the specified supply chain trade settlement.
	 *
	 * @return the specified supply chain trade settlement
	 */
	public SpecifiedSettlement getSettlement() {
		return settlement;
	}

	/**
	 * Sets the specified supply chain trade settlement.
	 *
	 * @param settlement the new specified supply chain trade settlement
	 * @return the item
	 */
	public Item setSettlement(SpecifiedSettlement settlement) {
		this.settlement = settlement;
		return this;
	}

	/**
	 * Gets the specified trade product.
	 *
	 * @return the specified trade product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * Sets the specified trade product.
	 *
	 * @param product the product
	 * @return the item
	 */
	public Item setProduct(Product product) {
		this.product = product;
		return this;
	}
}
