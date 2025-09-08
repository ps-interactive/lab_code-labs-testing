function add(a, b) {
  return a + b;
}

function subtract(a, b) {
  return a - b;
}

function multiply(a, b) {
  return a * b;
}

function divide(a, b) {
  if (b === 0) {
    throw new Error("Division by zero is not allowed");
  }
  return a / b;
}

function power(base, exponent) {
  return Math.pow(base, exponent);
}

function abs(a) {
  return Math.abs(a);
}

function sqrt(number) {
  if (number < 0) {
    throw new Error("Cannot calculate square root of negative number");
  }
  return Math.sqrt(number);
}

function percentage(number, percent) {
  return (number * percent) / 100;
}
