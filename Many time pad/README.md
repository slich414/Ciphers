# Many time pad vulnerability

One time pad is encryption that requires a single-use key that cannot be smaller than the message.

In this cipher each character of text is encrypted by combining it with the corresponding character from single-use key using modular addition.

Many time pad vulnerability happens when we use single-use key many times.

Let's assume that our plain text only contains letters and spaces. When we xor two characters we can get character that it ascii code is lower than 64, but when we xor character and space, result's ascii code always will be bigger or equal than 65.

To find a key we have to look at encrypted text and find at least one character from each column that is bigger or equal than 65. If column doesn't contain that character, we cannot find corresponding key character. Then we have to xor that character with space and as a result we get key.

 To fully recover key, we have to iterate through each column.

To decript message, we have to XOR encrypted text with key.



## Work modes:
### Text preparation:
For each line in orig.txt program removes:
- dots, commas, slashes
- numbers

What is more, text in each line above fixed size is cut.

### Encryption:

It iterate through each line of plain.txt file and xor each character with corresponding key character.


### Cryptoanalsis:

It iterate though each column of encrypted file and tries to find a character that have been XORed with space. If a column doesn't contain the character, all character in corresponding column in result file will be '?'.






## Encrypt example:

key: 

| 0 | 1 | 2 |
|---|---|---|
| k | e | y |


plain text:
| 0 | 1 | 2 |
|---|---|---|
| a | l | l |
| o |   | r |
| w | q |   |

encrypted text (ascii values):

| 0 | 1 | 2 |
|---|---|---|
|10 | 9 | 21| 
| 4 |69 |11 | 
|28 |20 |89 |

## Decript example:

encrypted text (ascii values):

| 0 | 1 | 2 |
|---|---|---|
|10 | 9 | 21| 
| 4 |69 |11 | 
|28 |20 |89 |

decripted text:
| 0 | 1 | 2 |
|---|---|---|
| ? | l | l |
| ? |   | r |
| ? |q  |   |

As you see, we couldn't find first column characters because all characters from encrypted text were lower than 64. But in second column, we can see that second line has a value 69. 69 ⊕ 32 (space) = 101 (e).

In last column, we can observe that in last line is value 89. 89 ⊕ 32 = 121 (y)

## Files explanation:
- xor.py - source code
- orig.txt - text file to encrypt
- plain.txt - orig.txt text after preparation
- key.txt - text file containing the key
- crypto.txt - encrypted text
- decript.txt - decrypted text

## Program arguments:
- -p - prepare text
- -e - encrypt
- -c - cryptanalysis


## How to use:
Remember that fixed key length is 32. You can change it in source code. Each text line in orig.txt must be at least 32 length.

```
./xor.py -argument1
or
./xor.py -argument1 -argument2
or
./xor.py -p -e -c

```