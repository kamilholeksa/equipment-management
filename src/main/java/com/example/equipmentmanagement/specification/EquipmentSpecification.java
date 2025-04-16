package com.example.equipmentmanagement.specification;

import com.example.equipmentmanagement.dto.equipment.EquipmentFilter;
import com.example.equipmentmanagement.model.Equipment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Set;

public class EquipmentSpecification {

    private EquipmentSpecification() {}

    public static Specification<Equipment> withId(Long id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Equipment> withManufacturer(String manufacturer) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("manufacturer")), "%" + manufacturer.toLowerCase() + "%");
    }

    public static Specification<Equipment> withModel(String model) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("model")), "%" + model.toLowerCase() + "%");
    }

    public static Specification<Equipment> withInventoryNumber(String inventoryNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("inventoryNumber")), "%" + inventoryNumber.toLowerCase() + "%");
    }

    public static Specification<Equipment> withSerialNumber(String serialNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("serialNumber")), "%" + serialNumber.toLowerCase() + "%");
    }

    public static Specification<Equipment> withStatusIn(Set<String> status) {
        return (root, query, criteriaBuilder) ->
                root.get("status").in(status);
    }

    public static Specification<Equipment> withLocation(String location) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }

    public static Specification<Equipment> withPurchaseDate(LocalDate purchaseDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("purchaseDate"), purchaseDate);
    }

    public static Specification<Equipment> withWarrantyUntil(LocalDate warrantyUntil) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("warrantyUntil"), warrantyUntil);
    }

    public static Specification<Equipment> withWithdrawalDate(LocalDate withdrawalDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("withdrawalDate"), withdrawalDate);
    }

    public static Specification<Equipment> withTypeId(Long typeId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("type").get("id"), typeId);
    }

    public static Specification<Equipment> withAddressId(Long addressId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("address").get("id"), addressId);
    }

    public static Specification<Equipment> withUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Equipment> prepareSpecification(EquipmentFilter filter) {
        Specification<Equipment> spec = Specification.where(null);

        if (filter.getId() != null) {
            spec = spec.and(withId(filter.getId()));
        }
        if (filter.getManufacturer() != null) {
            spec = spec.and(withManufacturer(filter.getManufacturer()));
        }
        if (filter.getModel() != null) {
            spec = spec.and(withModel(filter.getModel()));
        }
        if (filter.getInventoryNumber() != null) {
            spec = spec.and(withInventoryNumber(filter.getInventoryNumber()));
        }
        if (filter.getSerialNumber() != null) {
            spec = spec.and(withSerialNumber(filter.getSerialNumber()));
        }
        if (filter.getStatus() != null) {
            spec = spec.and(withStatusIn(filter.getStatus()));
        }
        if (filter.getLocation() != null) {
            spec = spec.and(withLocation(filter.getLocation()));
        }
        if (filter.getPurchaseDate() != null) {
            spec = spec.and(withPurchaseDate(filter.getPurchaseDate()));
        }
        if (filter.getWarrantyUntil() != null) {
            spec = spec.and(withWarrantyUntil(filter.getWarrantyUntil()));
        }
        if (filter.getWithdrawalDate() != null) {
            spec = spec.and(withWithdrawalDate(filter.getWithdrawalDate()));
        }
        if (filter.getTypeId() != null) {
            spec = spec.and(withTypeId(filter.getTypeId()));
        }
        if (filter.getAddressId() != null) {
            spec = spec.and(withAddressId(filter.getAddressId()));
        }
        if (filter.getUserId() != null) {
            spec = spec.and(withUserId(filter.getUserId()));
        }

        return spec;
    }
}
