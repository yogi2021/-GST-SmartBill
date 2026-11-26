
package com.yogii.gstsmartbill.service;

import com.yogii.gstsmartbill.dto.GstCalculationRequest;
import com.yogii.gstsmartbill.dto.GstCalculationResponse;
import com.yogii.gstsmartbill.dto.GstItemRequest;
import com.yogii.gstsmartbill.dto.GstItemResponse;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class GstCalculationService {

    private static final int SCALE = 2;

    private static final RoundingMode ROUNDING =
            RoundingMode.HALF_UP;

    public GstCalculationResponse calculateGST(
            GstCalculationRequest request) {

        // Compare seller and place-of-supply state codes
        boolean intraState = request.getSellerStateCode()
                .trim()
                .equalsIgnoreCase(
                        request.getPlaceOfSupplyStateCode().trim()
                );

        List<GstItemResponse> calculatedItems = new ArrayList<>();

        BigDecimal totalTaxable = BigDecimal.ZERO;
        BigDecimal totalCgst = BigDecimal.ZERO;
        BigDecimal totalSgst = BigDecimal.ZERO;
        BigDecimal totalIgst = BigDecimal.ZERO;

        // Calculate GST for every invoice item
        for (GstItemRequest item : request.getItems()) {

            BigDecimal quantity = item.getQuantity();
            BigDecimal unitPrice = item.getUnitPrice();
            BigDecimal gstRate = item.getGstRate();

            // Taxable amount = Quantity * Unit Price
            BigDecimal taxableAmount = quantity
                    .multiply(unitPrice)
                    .setScale(SCALE, ROUNDING);

            // GST = Taxable Amount * GST Rate / 100
            BigDecimal totalGst = taxableAmount
                    .multiply(gstRate)
                    .divide(new BigDecimal("100"), SCALE, ROUNDING);

            BigDecimal cgst = BigDecimal.ZERO;
            BigDecimal sgst = BigDecimal.ZERO;
            BigDecimal igst = BigDecimal.ZERO;

            if (intraState) {

                // Split GST equally into CGST and SGST
                cgst = totalGst.divide(
                        new BigDecimal("2"), SCALE, ROUNDING);

                // Keep the remaining paisa in SGST
                sgst = totalGst.subtract(cgst);

            } else {

                // Inter-state transaction
                igst = totalGst;
            }

            BigDecimal itemTotal = taxableAmount.add(totalGst)
                    .setScale(SCALE, ROUNDING);

            // Build item response
            GstItemResponse response = new GstItemResponse();

            response.setItemName(item.getItemName());
            response.setQuantity(quantity);
            response.setUnitPrice(unitPrice);
            response.setGstRate(gstRate);
            response.setTaxableAmount(taxableAmount);
            response.setCgstAmount(cgst);
            response.setSgstAmount(sgst);
            response.setIgstAmount(igst);
            response.setTotalGst(totalGst);
            response.setTotalAmount(itemTotal);

            calculatedItems.add(response);

            // Accumulate invoice totals
            totalTaxable = totalTaxable.add(taxableAmount);
            totalCgst = totalCgst.add(cgst);
            totalSgst = totalSgst.add(sgst);
            totalIgst = totalIgst.add(igst);
        }

        BigDecimal totalGst = totalCgst
                .add(totalSgst)
                .add(totalIgst)
                .setScale(SCALE, ROUNDING);

        // Invoice subtotal before rounding
        BigDecimal subTotal = totalTaxable.add(totalGst)
                .setScale(SCALE, ROUNDING);

        // Round invoice total to nearest whole rupee
        BigDecimal grandTotal = subTotal.setScale(0, ROUNDING)
                .setScale(SCALE, ROUNDING);

        BigDecimal roundOff = grandTotal.subtract(subTotal)
                .setScale(SCALE, ROUNDING);

        // Build final response
        GstCalculationResponse result = new GstCalculationResponse();

        result.setItems(calculatedItems);
        result.setTotalTaxableAmount(totalTaxable);
        result.setTotalCgst(totalCgst);
        result.setTotalSgst(totalSgst);
        result.setTotalIgst(totalIgst);
        result.setTotalGst(totalGst);
        result.setSubTotal(subTotal);
        result.setRoundOff(roundOff);
        result.setGrandTotal(grandTotal);

        return result;
    }
}
