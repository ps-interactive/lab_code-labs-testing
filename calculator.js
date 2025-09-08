class Calculator {
    constructor() {
        this.history = [];
        this.lastResult = 0;
    }

    /**
     * Adds two numbers
     * @param {number} a - First number
     * @param {number} b - Second number
     * @returns {number} Sum of a and b
     */
    add(a, b) {
        const result = a + b;
        this.recordOperation(`${a} + ${b} = ${result}`);
        this.lastResult = result;
        return result;
    }

    /**
     * Subtracts second number from first
     * @param {number} a - First number
     * @param {number} b - Second number
     * @returns {number} Difference of a and b
     */
    subtract(a, b) {
        const result = a - b;
        this.recordOperation(`${a} - ${b} = ${result}`);
        this.lastResult = result;
        return result;
    }

    /**
     * Multiplies two numbers
     * @param {number} a - First number
     * @param {number} b - Second number
     * @returns {number} Product of a and b
     */
    multiply(a, b) {
        const result = a * b;
        this.recordOperation(`${a} × ${b} = ${result}`);
        this.lastResult = result;
        return result;
    }

    /**
     * Divides first number by second
     * @param {number} a - Dividend
     * @param {number} b - Divisor
     * @returns {number} Quotient of a and b
     * @throws {Error} When dividing by zero
     */
    divide(a, b) {
        if (b === 0) {
            throw new Error("Division by zero is not allowed");
        }
        const result = a / b;
        this.recordOperation(`${a} ÷ ${b} = ${result}`);
        this.lastResult = result;
        return result;
    }

    /**
     * Calculates power (a^b)
     * @param {number} a - Base
     * @param {number} b - Exponent
     * @returns {number} a raised to the power of b
     */
    power(a, b) {
        const result = Math.pow(a, b);
        this.recordOperation(`${a}^${b} = ${result}`);
        this.lastResult = result;
        return result;
    }

    /**
     * Calculates square root
     * @param {number} a - Number to find square root of
     * @returns {number} Square root of a
     * @throws {Error} When calculating square root of negative number
     */
    sqrt(a) {
        if (a < 0) {
            throw new Error("Cannot calculate square root of negative number");
        }
        const result = Math.sqrt(a);
        this.recordOperation(`√${a} = ${result}`);
        this.lastResult = result;
        return result;
    }

    /**
     * Calculates percentage
     * @param {number} value - Base value
     * @param {number} percent - Percentage
     * @returns {number} Percentage of the value
     */
    percentage(value, percent) {
        const result = (value * percent) / 100;
        this.recordOperation(`${percent}% of ${value} = ${result}`);
        this.lastResult = result;
        return result;
    }

    /**
     * Gets the last calculated result
     * @returns {number} Last result
     */
    getLastResult() {
        return this.lastResult;
    }

    /**
     * Gets calculation history
     * @returns {string[]} Array of operation strings
     */
    getHistory() {
        return [...this.history];
    }

    /**
     * Clears calculation history
     */
    clearHistory() {
        this.history = [];
    }

    /**
     * Resets calculator (clears history and last result)
     */
    reset() {
        this.history = [];
        this.lastResult = 0;
    }

    /**
     * Records operation in history
     * @private
     * @param {string} operation - Operation string to record
     */
    recordOperation(operation) {
        this.history.push(operation);
        // Keep only last 50 operations
        if (this.history.length > 50) {
            this.history.shift();
        }
    }
}

// Usage example:
const calc = new Calculator();
console.log(calc.add(10, 5)); // 15
console.log(calc.multiply(3, 4)); // 12
console.log(calc.divide(20, 4)); // 5
console.log(calc.getHistory()); // Array of operations
