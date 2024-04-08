import sys
import selectors
import types
import socket
import random

deck = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51]
clubs = [0,1,2,3,4,5,6,7,8,9,10,11,12]
dimonds = [13,14,15,16,17,18,19,20,21,22,23,24,25]
hearts = [26,27,28,29,30,31,32,33,34,35,36,37,38]
spades = [39,40,41,42,43,44,45,46,47,48,49,50,51]
cardsSeen = []
player1Deck = []
player2Deck = []
players = []

sel = selectors.DefaultSelector()

# check if the numbers are between 0-51, and have not been seen yet. return 1 if player one is caught cheating,
# 2 if player two is caught cheating, 3 if they are both cheating else 0. then add cards to cards seen
def antiCheat(cardOne, cardTwo):
    playerOne = False
    playerTwo = False
    if cardOne < 0 or cardOne > 51 or cardOne in cardsSeen:
        playerOne = True
    if cardTwo < 0 or cardTwo > 51 or cardTwo in cardsSeen:
        playerTwo = True

    if playerOne and not playerTwo:
        return 1
    elif playerTwo and not playerOne:
        return 2
    elif playerOne and playerTwo:
        return 3
    cardsSeen.append(cardOne)
    cardsSeen.append(cardTwo)
    return 0

# takes two cards represented by intagers 0-52.
# returns a -1 if card one is smaller, 0 if they are the same, or 1 if card one is larger.
# 0-12 are clubs, 13-25 are dimonds, 26-38 are hearts, 39-51 are spades.
# clubs are taken at int value, dimonds-13, hearts-26, spades -39.
def compareCards(cardOne,cardTwo):

    # get card value of cardOne
    if cardOne in dimonds:
        cardOne = cardOne - 13
    elif cardOne in hearts:
        cardOne = cardOne - 26
    elif cardOne in spades:
        cardOne = cardOne - 39
    # get card value of cardTwo
    if cardTwo in dimonds:
        cardTwo = cardTwo - 13
    elif cardTwo in hearts:
        cardTwo = cardTwo - 26
    elif cardTwo in spades:
        cardTwo = cardTwo - 39

    if cardOne < cardTwo:
        return -1
    elif cardOne == cardTwo:
        return 0

    return 1

def shuffle():
    # split up deck into two random decks. send to players.
    random.shuffle(deck)
    temp = deck[:len(deck)//2]
    for i in temp:
        player1Deck.append(i)
    temp = deck[len(deck)//2:]
    for i in temp:
        player2Deck.append(i)

# set up connection, listen for two players, shuffle, deal, listen for cards sent, make sure they are legal,
def main():
    host = socket.gethostname()
    port = 4444


    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    s.bind((host, port))
    s.listen()
    print(f'waiting for connection on {(host,port)}')
    s.setblocking(False)
    sel.register(s, selectors.EVENT_READ, data=None)

    try:
        while True:
            events = sel.select(timeout=None)
            for key, mask in events:
                if key.data is None:
                    accept_Wrapper(key.fileobj)
                else:
                    service_Connection(key, mask)

            if len(players) == 2:
                gameLoby(players[0], players[1])
                players.clear()

    except KeyboardInterrupt:
        print("closing connection")
    finally:
        sel.close()




    # listen for a card from each player, log the cards, run through anti-cheat, compare the cards, count the winner
    # then tell the players who won and ask for the next card.
    return



def accept_Wrapper(sock):
    conn, addr = sock.accept()  # Should be ready to read
    print(f"Accepted connection from {addr}")
    conn.setblocking(False)
    data = types.SimpleNamespace(addr=addr, inb=b"", outb=b"")
    events = selectors.EVENT_READ | selectors.EVENT_WRITE
    sel.register(conn, events, data=data)

def service_Connection(key, mask):
    sock = key.fileobj
    data = key.data
    if mask & selectors.EVENT_READ:
        recv_data = sock.recv(1)  # Should be ready to read
        if recv_data:
            data.outb += recv_data
        else:
            print(f"Closing connection to {data.addr}")
            sel.unregister(sock)
            sock.close()
    if mask & selectors.EVENT_WRITE:
        if data.outb == b'0' and sock not in players:
            print(f"finding a game")
            sent = sock.send(data.outb)  # Should be ready to write
            data.outb = data.outb[sent:]
            players.append(sock)


def gameLoby(player1, player2):

    # game start. shuffle the deck and deal out the cards
    player1Wins = 0
    player2Wins = 0
    player1.send(b'1')  # game start
    player2.send(b'1')
    shuffle()
    pchunk = b''
    for i in player1Deck:
        pchunk = pchunk + i.to_bytes(1,'big')
    player1.send(pchunk)
    pchunk = b''
    for i in player2Deck:
        pchunk = pchunk + i.to_bytes(1,'big')
    player2.send(pchunk)

    # ask for a card from each player. run through anti cheat, compare the values. send the results.
    player1.setblocking(True)
    player2.setblocking(True)
    while len(cardsSeen)<len(deck):
        player1.send(b'2') #play a card
        p1CardData = player1.recv(1)

        player2.send(b'2')
        p2CardData = player2.recv(1)

        p1Card = int.from_bytes(p1CardData,'big')
        p2Card = int.from_bytes(p2CardData,'big')

        caught = antiCheat(p1Card, p2Card)
        if caught != 0:
            if caught == 1:
                print("player1 is cheating")
            elif caught == 2:
                print("player2 is cheating")
            else:
                print('they are both cheating!')
            sel.unregister(player1)
            player1.close()
            sel.unregister(player2)
            player2.close()
            return
        else:
            result = compareCards(p1Card,p2Card)

            if result == -1:
                print('player1 hand')
                player1Wins += 1
                player1.send(b'0')  # win
                player2.send(b'2')  # lost
            elif result == 1:
                print('player2 hand')
                player2Wins += 1
                player1.send(b'2')  # lost
                player2.send(b'0')  # win
            else:
                print('its a tie')
                player1.send(b'1')  # tie
                player2.send(b'1')  # tie

    #end while
    if player1Wins>player2Wins:
        print("1 wins")
    elif player2Wins > player1Wins:
        print("2 wins")
    else:
        print('its a tie!')

    print('thanks for playing!')
    cardsSeen.clear()
    player1Deck.clear()
    player2Deck.clear()
    players.clear()
    return

if __name__=='__main__':
    main()