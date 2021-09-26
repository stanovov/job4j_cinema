let SESSION_ID;
let ROW;
let CELL;

$(document).ready(function () {
    initMasks();
    outTitle();
});

function initMasks() {
    $(":input").inputmask();
    $("#phone").inputmask({
        mask: '+7 (999) 999-99-99',
        placeholder: ' ',
        showMaskOnHover: false,
        showMaskOnFocus: false,
        onBeforePaste: function (pastedValue, opts) {
            var processedValue = pastedValue;
            return processedValue;
        }
    });
}

function outTitle() {
    let params = new URLSearchParams(document.location.search.substring(1));
    SESSION_ID = +params.get("session_id");
    ROW = +params.get("row");
    CELL = +params.get("cell");
    let session = null;
    if (SESSION_ID === 1) {
        session = '10:00';
    } else if (SESSION_ID === 2) {
        session = '13:30';
    } else if (SESSION_ID === 3) {
        session = '17:00';
    } else if (SESSION_ID === 4) {
        session = '20:30';
    } else if (SESSION_ID === 5) {
        session = '00:00';
    }
    $('#title').text(`Вы выбрали сеанс на ${session} ряд ${ROW} место ${CELL}, Сумма : 500 рублей.`);
}

function parsePhone() {
    return $('#phone').val()
        .replaceAll(' ', '')
        .replaceAll('-', '')
        .replace(')', '')
        .replace('(', '');
}

function validate() {
    let items = [];
    let phone = parsePhone();
    if ($('#username').val() === '') {
        items.push('ФИО');
    }
    if (phone === '' || phone.length !== 12) {
        items.push('Номер телефона');
    }
    let text = '';
    if (items.length > 0) {
        for (let item of items) {
            text += (text === '') ? item : ', ' + item;
        }
        text = 'Необходимо заполнить обязательные поля: ' + text;
    }
    $('#notification').text(text);
    return items.length === 0;
}

function payment() {
    $('#notification').prop("style", "color: red");
    if (validate()) {
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/cinema/hall',
            data: JSON.stringify({
                sessionId: SESSION_ID,
                row: ROW,
                cell: CELL,
                account: {
                    username: $('#username').val(),
                    email: $('#email').val(),
                    phone: parsePhone()
                }
            }),
            dataType: 'text'
        }).done(function(data) {
            if (data === '200 OK') {
                $('#notification').prop("style", "color: green");
                $('#notification').text('Билет успешно забронирован. '
                    + 'Через несколько секунд Вас перенаправит на главную страницу...');
                setTimeout(() => {window.location.href = 'http://localhost:8080/cinema/index.html'}, 5000);
            } else {
                $('#notification').text(data);
            }
        }).fail(function(err) {
            console.log(err);
        });
    }
}