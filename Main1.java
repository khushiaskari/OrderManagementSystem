
    import java.text.SimpleDateFormat;
import java.util.*;

class Product {
    private int id;
    private String name;
    private String category;
    private double price;

    public Product(int prodID, String prodName, String prodCategory, double prodPrice) {
        this.id = prodID;
        this.name = prodName;
        this.category = prodCategory;
        this.price = prodPrice;
    }

    public int getID() { 
        return id;
    }

    public String getName() { 
        return name; 
    }

    public String getCategory() { 
        return category; 
    }

    public double getPrice() { 
        return price; 
    }

    public void displayProduct() {
        System.out.println("ProductID: " + id 
            + ", Name: " + name 
            + ", Category: " + category 
            + ", Price: $" + price);
    }
}

class Order {
    private int orderID;
    private String customerID;
    private Date orderDate;
    private List<int[]> orderItems; // each element: {productID, quantity}

    public Order(int ordID, String custID, Date date) {
        this.orderID = ordID;
        this.customerID = custID;
        this.orderDate = date;
        this.orderItems = new ArrayList<>();
    }

    public void addItem(int productID, int quantity) {
        orderItems.add(new int[]{productID, quantity});
    }

    public List<int[]> getOrderItems() {
        return orderItems;
    }

    public double calculateTotal(Map<Integer, Product> productCatalog) {
        double total = 0;
        for (int[] item : orderItems) {
            Product product = productCatalog.get(item[0]);
            if (product != null) {
                total += product.getPrice() * item[1];
            }
        }
        return total;
    }

    public void displayOrder(Map<Integer, Product> productCatalog) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        System.out.println("\nOrderID: " + orderID 
            + ", CustomerID: " + customerID 
            + ", OrderDate: " + sdf.format(orderDate));
        System.out.println("Items in Order:");
        for (int[] item : orderItems) {
            Product product = productCatalog.get(item[0]);
            if (product != null) {
                double subtotal = product.getPrice() * item[1];
                System.out.println("ProductID: " + item[0] 
                    + ", Name: " + product.getName() 
                    + ", Quantity: " + item[1] 
                    + ", Price: $" + product.getPrice() 
                    + ", Subtotal: $" + subtotal);
            }
        }
        System.out.println("Total Bill: $" + calculateTotal(productCatalog));
    }
}

public class Main1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Create product catalog
        Map<Integer, Product> productCatalog = new HashMap<>();
        Map<Integer, Integer> productStock = new HashMap<>();

        System.out.print("Enter number of products to add in catalog: ");
        int n = sc.nextInt();
        sc.nextLine(); // consume newline

        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for Product " + (i+1));
            System.out.print("Product ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Product Name: ");
            String name = sc.nextLine();
            System.out.print("Category: ");
            String category = sc.nextLine();
            System.out.print("Price: ");
            double price = sc.nextDouble();
            System.out.print("Stock Quantity: ");
            int stock = sc.nextInt();

            productCatalog.put(id, new Product(id, name, category, price));
            productStock.put(id, stock);
        }

        // Create an order
        System.out.print("\nEnter Customer ID: ");
        String customerID = sc.next();
        System.out.print("Enter Order ID: ");
        int orderID = sc.nextInt();

        Date now = new Date();
        Order order = new Order(orderID, customerID, now);

        // Add products to order
        while (true) {
            System.out.print("\nEnter Product ID to add to order (0 to finish): ");
            int prodID = sc.nextInt();
            if (prodID == 0) break;

            if (!productCatalog.containsKey(prodID)) {
                System.out.println("Invalid Product ID.");
                continue;
            }

            System.out.print("Enter Quantity: ");
            int qty = sc.nextInt();

            if (productStock.get(prodID) >= qty) {
                order.addItem(prodID, qty);
                productStock.put(prodID, productStock.get(prodID) - qty);
            } else {
                System.out.println("Insufficient stock! Available: " + productStock.get(prodID));
            }
        }

        // Display all products
        System.out.println("\nAvailable Products:");
        for (Product product : productCatalog.values()) {
            product.displayProduct();
        }

        // Display order summary
        System.out.println("\nOrder Summary:");
        order.displayOrder(productCatalog);

        // Show updated stock
        System.out.println("\nUpdated Stock Levels:");
        for (Map.Entry<Integer, Integer> entry : productStock.entrySet()) {
            System.out.println("ProductID: " + entry.getKey() + ", Stock: " + entry.getValue());
        }

        sc.close();
    }
}
    
