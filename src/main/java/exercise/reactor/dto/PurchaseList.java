package exercise.reactor.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;

@JsonRootName("purchases")
public class PurchaseList extends ArrayList<Purchase> {
}
