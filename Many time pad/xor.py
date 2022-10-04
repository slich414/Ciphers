import sys

def getByteKeyArray():
    key = open("key.txt", "r")
    key = key.read()
    ascii_values = []
    for character in key:
        ascii_values.append(ord(character))
    return ascii_values

def text_preparing(size):
    orig = open("orig.txt", 'r')
    plain = open("plain.txt", 'w')
    replace = [',', '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '\'']
    for line in orig:
        for element in replace:
            line = line.replace(element, '')
        line = line[0:size]
        plain.write(line)
        plain.write("\n")
    
def encrypt(size):
    plain = open("plain.txt", "r")
    crypto = open("crypto.txt", "w")
    key = getByteKeyArray()
    size = size
    for line in plain:
        for i in range(0, size):
            result = ord(line[i]) ^ key[i]
            crypto.write(str(result) + " ")
        crypto.write("\n")


def cryptoanalysis_based_on_cryptogram(size):
    size = size
    crypto_array = []

    file = open('crypto.txt', 'r')
    line = file.readline()
    while line:
        line = line.strip()
        my_array = [int(x) for x in line.split()]
        crypto_array.append(my_array)
        line = file.readline()

    key = bytearray(b'?' * size)

    for k in range(0, size):
        cts = crypto_array
        for curs1 in range(len(cts)):
            character = cts[curs1][k]
            if character > 64:
                found_key = character ^ ord(' ')
                key[k] = found_key

    result = [ ['?']*size for i in range(len(crypto_array))]
    for i in range(0, size):
        for curs1 in range(len(crypto_array)):
            
            if key[i] == 63:
                result[curs1][i] = '?'
                continue
            else:
                wynik = crypto_array[curs1][i] ^ key[i]
                result[curs1][i] = chr(wynik)

            
    decript = open("decript.txt", 'w')
    for i in range(len(crypto_array)):
        for curs1 in range(0, size):
            decript.write(result[i][curs1])
        decript.write("\n")
        
        
size = 32


#No arguments
if(len(sys.argv)-1 == 0):
    print("You didn't insert any argument")
    
#One argument
elif len(sys.argv)-1 == 1:
    if sys.argv[1] == '-p':
        text_preparing(size)
    elif sys.argv[1] == '-e':
        encrypt(size)
    elif sys.argv[1] == '-c':
        cryptoanalysis_based_on_cryptogram(size)
        
#2 arguments
elif len(sys.argv)-1 == 2:
    if sys.argv[1] == '-p':
        text_preparing(size)
        if sys.argv[2] == '-e':
            encrypt(size)
        elif sys.argv[2] == '-c':
            cryptoanalysis_based_on_cryptogram(size)
    
    if sys.argv[1] == '-e':
        encrypt(size)
        if sys.argv[2] == '-c':
            cryptoanalysis_based_on_cryptogram(size)


elif len(sys.argv)-1 == 3:
    text_preparing(size)
    encrypt(size)
    cryptoanalysis_based_on_cryptogram(size)