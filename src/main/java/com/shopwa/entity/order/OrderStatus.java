package com.shopwa.entity.order;

public enum OrderStatus {
    NEW{
        @Override
        public String defaultDescription() {
            return "Order was placed by the customer";
        }
    },
    CANCELLED{
        @Override
        public String defaultDescription() {
            return "Order was reject";
        }
    },
    PROCESSING{
        @Override
        public String defaultDescription() {
            return "Order is being processed";
        }
    },
    PACKAGED{
        @Override
        public String defaultDescription() {
            return "Products were packaged";
        }
    },
    PICKED{
        @Override
        public String defaultDescription() {
            return "Shipper picked the package";
        }
    },
    SHIPPING{
        @Override
        public String defaultDescription() {
            return "Shipper is delivering the package";
        }
    },
    DELIVERED{
        @Override
        public String defaultDescription() {
            return "Customer was received products";
        }
    },
    RETURNED{
        @Override
        public String defaultDescription() {
            return "Products were returned";
        }
    },
    PAID{
        @Override
        public String defaultDescription() {
            return "Customer has paid this package";
        }
    },
    RETURN_REQUESTED{
        @Override
        public String defaultDescription() {
            return "Customer sent request to return purchase";
        }
    },
    REFUNDED{
        @Override
        public String defaultDescription() {
            return "Customer has refunded";
        }
    };

    public abstract String defaultDescription();

}
