
# Data Modelling of JSON docs

## Problem Statement

The input data is in a simple format: Each file contains a root object with one  
member `entries`. It defines a list of data points and each data point can  
contain a variable number of members with child objects. In our example, each  
data point is a city and has three members. One member is mandatory, this is  
called `name` and is used for filtering.

The operations are similar in structure. The file contains an object with the  
member `operations` which defines a list of operation objects. Each operation always  
has these four members:

- `name` – The name to be used for the output.
- `function` – The function to be evaluated, this can be `min`, `max`, `sum` or `average`.
- `field` – An array of member names to access the value for the operation.
- `filter` – A regular expression to be applied to the `name` member. Only  
  entries matching the regular expression should be included in the evaluation.

The output also consists of a list of objects which contain the operation name  
and the formatted calculated value. Floating point numbers are to be written  
with exactly two decimal places.

Attached you will find three example files that perform such an evaluation.


## Files

The following files are supplied:

- `data.json` – Example of input data.
- `operations.json` – Example of operations to be calculated.
- `output.json` – The results for the sample data provided.
- `JsonCalculator.java` The entry point for the implementation  