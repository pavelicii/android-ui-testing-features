package pavelnazimok.uitestingfeatures;

class CoffeeOrder {

    private int coffeePrice;
    private int coffeeCount;
    private int totalPrice;

    CoffeeOrder(int coffeePrice) {
        coffeeCount = 0;
        totalPrice = 0;
        this.coffeePrice = coffeePrice;
    }

    int getCoffeeCount() {
        return coffeeCount;
    }

    int getTotalPrice() {
        return totalPrice;
    }

    void incrementCoffeeCount() {
        coffeeCount++;
        calculateTotalPrice();
    }

    void decrementCoffeeCount() {
        if (coffeeCount > -1) {
            coffeeCount--;
            calculateTotalPrice();
        }
    }

    private void calculateTotalPrice() {
        totalPrice = coffeePrice * coffeeCount;
    }
}
