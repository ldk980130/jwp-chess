const rows = [8, 7, 6, 5, 4, 3, 2, 1];
const columns = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'];

let source = '';
let target = '';
let turn = '';

function resetSourceAndTarget() {
    source = '';
    target = '';
}

async function selectPiece(position) {
    let piece = await document.getElementById('piece-' + position);
    if (piece.className === 'piece active') {
        event.stopPropagation();
    }
    let isActive = piece.className.split(' ')[1];
    if (isActive && source === '') {
        source = position;
        lockPiece();
        unlockSquare();
    }
}

function selectSquare(position) {
    let square = document.getElementById('square-' + position);
    let isActive = square.className.split(' ')[1];
    if (isActive && target === '') {
        target = position;
        lockSquare();
    }
    if (source !== '' && target !== '') {
        fetchMove(source, target);
    }
}

function lockPiece() {
    let pieces = document.getElementsByClassName('piece');
    for (const piece of pieces) {
        piece.className = piece.className.split(' ')[0];
    }
}

function lockSquare() {
    let squares = document.getElementsByClassName('square');
    for (const square of squares) {
        square.className = square.className.split(' ')[0];
    }
}

function unlockPiece() {
    let pieces = document.getElementsByClassName('piece');
    for (const piece of pieces) {
        piece.className = piece.className + ' active';
    }
}

function unlockSquare() {
    let squares = document.getElementsByClassName('square');
    for (const square of squares) {
        square.className = 'square active';
        square.style.pointerEvents = 'auto';
    }
}

function initBlocks(piecesContainer) {
    for (const row of rows) {
        for (const column of columns) {
            let square = document.createElement('div');
            square.className = 'square';
            square.id = 'square-' + column + row;
            square.onclick = function () {selectSquare(column + row)};
            piecesContainer.append(square);
        }
    }
}

function removeOldPieces() {
    for (const row of rows) {
        for (const column of columns) {
            let oldPiece = document.getElementById('piece-' + column + row);
            if (oldPiece) {
                oldPiece.remove();
            }
        }
    }
}

function setNewPieces(pieces) {
    for (const piece of pieces) {
        let square = document.getElementById('square-' + piece.position);
        let pieceImage = document.createElement('img');
        pieceImage.src = '/images/' + piece.color + '_' + piece.role + '.png';
        pieceImage.className = 'piece' + ' active';
        pieceImage.id = 'piece-' + piece.position;
        pieceImage.onclick = function () {selectPiece(piece.position)};
        square.append(pieceImage);
    }
}

async function initPieces(pieces) {
    let oldPieces = await document.getElementsByClassName('piece');
    await removeOldPieces(oldPieces);
    await setNewPieces(pieces);
    resetSourceAndTarget();
}

function toggleState(state) {
    turn = state.turn;
    let whiteIcon = document.getElementById('white-icon');
    let blackIcon = document.getElementById('black-icon');

    if (turn === 'white') {
        whiteIcon.className = 'room-icon on';
        blackIcon.className = 'room-icon off';
    } else {
        whiteIcon.className = 'room-icon off';
        blackIcon.className = 'room-icon on';
    }

    document.getElementById('white-message').innerHTML = '';
    document.getElementById('black-message').innerHTML = '';
    end = state.end;
    if (end === 'true') {
        disableLoadGame();
        return;
    }
    activateLoadGame();
}

function showErrorMessage(message) {
    if (turn === 'white') {
        document.getElementById('white-message').innerHTML = message;
        return;
    }
    document.getElementById('black-message').innerHTML = message;
}

function updateResult(result) {
    document.getElementById('white-score').innerHTML = result.whiteScore + '점';
    document.getElementById('black-score').innerHTML = result.blackScore + '점';
    if (result.winner !== 'none') {
        document.getElementById(result.winner + '-score').innerHTML += ' 승리 중';
    }
}

function checkEnd(state) {
    if (state.end === 'true') {
        fetchFinalResult();
    }
}

function showFinalResult(result) {
    lockPiece();
    lockSquare();
    let winner = result.winner;
    document.getElementById('white-score').innerHTML = result.whiteScore + '점';
    document.getElementById('black-score').innerHTML = result.blackScore + '점';
    document.getElementById(winner + '-score').innerHTML += ' 승리';

    if (winner === 'white') {
        document.getElementById('white-icon').className = 'room-icon';
        document.getElementById('black-icon').className = 'room-icon off';
    } else {
        document.getElementById('white-icon').className = 'room-icon off';
        document.getElementById('black-icon').className = 'room-icon';
    }
}

function resetScores() {
    document.getElementById('white-score').innerHTML = '-점';
    document.getElementById('black-score').innerHTML = '-점';
}

function setBoardId(id) {
    document.getElementById("board-id").value = id;
}

function disableLoadGame() {
    document.getElementById('game-state-end').value = 'true';
}

function activateLoadGame() {
    document.getElementById('game-state-end').value = 'false';
}

window.onload = async function () {
    let url = location.href;
    let urlArr = url.split('/');
    let roomId = urlArr[urlArr.length-1];
    document.getElementById("room-id").value = roomId;

    let piecesContainer = document.getElementById('pieces-container');
    await initBlocks(piecesContainer);
}

function fetchNewChess() {
    let roomId = document.getElementById("room-id").value;
    fetch('http://localhost:8080/rooms/' + roomId + '?command=start', {
        method: 'GET',
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
    })
        .then(res => res.json())
        .then(res => {
            setBoardId(res.boardId);
            resetScores();
            toggleState(res.state);
            initPieces(res.pieces);
            activateLoadGame();
        })
}

function fetchLoadChess() {
    let end = document.getElementById('game-state-end').value;
    if (end === 'true') {
        alert("게임이 종료되었습니다. 새 게임을 눌러주세요.");
        return;
    }
    let roomId = document.getElementById("room-id").value;
    fetch('http://localhost:8080/rooms/' + roomId + '?command=load', {
        method: 'GET',
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
    })
        .then(res => res.json())
        .then(res => {
            setBoardId(res.boardId);
            resetScores();
            toggleState(res.state);
            initPieces(res.pieces);
        })
}


function fetchMove(source, target) {
    let boardId = document.getElementById("board-id").value;
    fetch('http://localhost:8080/boards/' + boardId + '?command=move', {
        method: 'POST',
        body: JSON.stringify({
            source: source,
            target: target
        }),
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
    })
        .then(res => res.json())
        .then(res => {
            if (res.message) {
                throw new Error(res.message);
            }
            toggleState(res.state);
            initPieces(res.pieces);
            checkEnd(res.state);
        })
        .catch(err => {
            showErrorMessage(err.message);
            resetSourceAndTarget();
            lockSquare();
            unlockPiece();
        })
}

function fetchResult() {
    let end = document.getElementById('game-state-end').value;
    if (end === 'true') {
        alert("게임이 종료되었습니다. 새 게임을 눌러주세요.");
        return;
    }
    let boardId = document.getElementById("board-id").value;
    fetch('http://localhost:8080/boards/' + boardId + '?command=result', {
        method: 'GET',
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
    })
        .then(res => res.json())
        .then(res => updateResult(res));
}

function fetchFinalResult() {
    let boardId = document.getElementById("board-id").value;
    fetch('http://localhost:8080/boards/' + boardId + '?command=end', {
        method: 'GET',
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
    })
        .then(res => res.json())
        .then(res => {
            disableLoadGame();
            showFinalResult(res);
        });
}