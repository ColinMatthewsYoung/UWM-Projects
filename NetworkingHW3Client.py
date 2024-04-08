import socket

host = socket.gethostname()
port = 4444
win = 0
loss = 0
with socket.socket(socket.AF_INET,socket.SOCK_STREAM) as s:
    s.connect((host, port))
    while True:
        s.sendall(b'0') # want game
        data = s.recv(1)
        if b'0' in data:
            print(f"Received {data!r}")
            break
    while b'1' not in data: # game start
        data = s.recv(1)

    data = s.recv(26)
    # try and change this to bytes...
    deck = [data[i:i+1] for i in range(0,len(data),1)]

    data = b''
    for i in deck:
        while b'2' not in data: # play card
            data = s.recv(1)
        s.send(i)
        data = b''
        while data == b'':
            data = s.recv(1)
        if b'0' in data:
            print('we won the hand')
            win = win + 1
        elif b'2' in data:
            print('we lost the hand')
            loss = loss + 1
        else:
            print('it was a tie')
        data = b''
        # end while

    if win > loss:
        print("we won the game!")
    elif win < loss:
        print('we lost the game!')
    else:
        print("it was a tie game! who's shuffling this deck?")

    s.close()