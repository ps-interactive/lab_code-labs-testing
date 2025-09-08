export class Calculator {
    private history: string[] = [];
    private lastResult: number = 0;

    /**
     * Adds two numbers
     */
    add(a: number, b: number): number {
        const result = a + b;
        this.recordOperation(`${a} + ${b} = ${result}`);
        this.lastResult = result;
        return result;
    }

    /**
     * Subtracts second number from first
     */
    subtract(a: number, b: number): number {
        const result = a - b;
        this.recordOperation(`${a} - ${b} = ${result}`);
        this.lastResult = result;
        return result;
    }

    /**
     * Multiplies two numbers
     */
    multiply(a: number, b: number): number {
        const result = a * b;
        this.recordOperation(`${a} × ${b} = ${result}`);
        this.lastResult = result;
        return result;
    }

    /**
     * Divides first number by second
     */
    divide(a: number, b: number): number {
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
     */
    power(a: number, b: number): number {
        const result = Math.pow(a, b);
        this.recordOperation(`${a}^${b} = ${result}`);
        this.lastResult = result;
        return result;
    }

    /**
     * Calculates square root
     */
    sqrt(a: number): number {
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
     */
    percentage(value: number, percent: number): number {
        const result = (value * percent) / 100;
        this.recordOperation(`${percent}% of ${value} = ${result}`);
        this.lastResult = result;
        return result;
    }

    /**
     * Gets the last calculated result
     */
    getLastResult(): number {
        return this.lastResult;
    }

    /**
     * Gets calculation history
     */
    getHistory(): string[] {
        return [...this.history];
    }

    /**
     * Clears calculation history
     */
    clearHistory(): void {
        this.history = [];
    }

    /**
     * Resets calculator (clears history and last result)
     */
    reset(): void {
        this.history = [];
        this.lastResult = 0;
    }

    /**
     * Records operation in history
     */
    private recordOperation(operation: string): void {
        this.history.push(operation);
        // Keep only last 50 operations
        if (this.history.length > 50) {
            this.history.shift();
        }
    }
}
