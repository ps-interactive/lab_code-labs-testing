// Test file for tree-sitter query functionality
function Calculator() {
    this.value = 0;
}

Calculator.prototype.add = function(num) {
    this.value += num;
    return this;
};

Calculator.prototype.subtract = function(num) {
    this.value -= num;
    return this;
};

function createCalculator() {
    return new Calculator();
}

const calc = createCalculator();
calc.add(5).subtract(2);
