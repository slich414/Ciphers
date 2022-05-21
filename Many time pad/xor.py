import sys

def getByteKeyArray():
    key = open("key.txt", "r")
    key = key.read()
    ascii_values = []
    for character in key:
        ascii_values.append(ord(character))
    return ascii_values

def przygotowanie_tekstu(size):
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


def kryptoanaliza_w_oparciu_o_kryptogram(size):
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
            znak = cts[curs1][k]
            if znak > 64:
                moj_klucz = znak ^ ord(' ')
                key[k] = moj_klucz

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


#Jezeli nie ma zadnego argumentu
if(len(sys.argv)-1 == 0):
    print("Nie podales zadnego argumentu")
    
#Jezeli jest jeden argument
elif len(sys.argv)-1 == 1:
    if sys.argv[1] == '-p':
        przygotowanie_tekstu(size)
    elif sys.argv[1] == '-e':
        encrypt(size)
    elif sys.argv[1] == '-k':
        kryptoanaliza_w_oparciu_o_kryptogram(size)
        
#Jezeli 2 argumenty
elif len(sys.argv)-1 == 2:
    if sys.argv[1] == '-p':
        przygotowanie_tekstu(size)
        if sys.argv[2] == '-e':
            encrypt(size)
        elif sys.argv[2] == '-k':
            kryptoanaliza_w_oparciu_o_kryptogram(size)
    
    if sys.argv[1] == '-e':
        encrypt(size)
        if sys.argv[2] == '-k':
            kryptoanaliza_w_oparciu_o_kryptogram(size)


elif len(sys.argv)-1 == 3:
    przygotowanie_tekstu(size)
    encrypt(size)
    kryptoanaliza_w_oparciu_o_kryptogram(size)