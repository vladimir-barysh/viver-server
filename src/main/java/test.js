const Stomp = require('stompjs');
const SockJS = require('sockjs-client');

const socket = new SockJS('http://localhost:8080/chat');
console.log('Attempting to connect to SockJS...');

socket.onopen = function() {
    console.log('WebSocket opened');
};

socket.onclose = function(event) {
    console.log('WebSocket closed with code: ' + event.code + ', reason: ' + event.reason);
};

socket.onerror = function(error) {
    console.log('WebSocket error: ' + JSON.stringify(error));
};

const stompClient = Stomp.over(socket);

stompClient.connect(
    { Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMiIsImlhdCI6MTc0ODA3NDM4MCwiZXhwIjoxNzQ4MTYwNzgwfQ.uQp49JtTimqTmvYas_aW2htB9TvwB1JqkOkr6bSK2yv4vyIMwk84vfSTLgLmjBmABdDoGbWj_wOxOM5R3WzuhA" },
    function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function(message) {
            console.log('Message received: ' + message.body);
        });
        stompClient.send('/app/message', {}, JSON.stringify({
            id: 2,
            senderId: 2,
            recipientId: 1,
            content: 'Test from JS',
            timestamp: '2025-05-24T14:00:00'
        }));
    },
    function(error) {
        console.log('Connection error details: ' + JSON.stringify(error));
    }
);