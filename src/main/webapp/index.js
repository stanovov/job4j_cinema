let session = 1;
const timeout = 15000;

$(document).ready(function () {
    getTickets();
    setTimeout(refresh, timeout);
});

function refresh(){
    getTickets();
    setTimeout(refresh, timeout);
}

function changeSession(vSession) {
    if (session !== vSession) {
        session = vSession;
        getTickets();
    }
}

function getTickets() {
    $('input:radio').each(function() {
        if (this.name === 'place') {
            $(this).prop("disabled", false);
            $(this).prop("checked", false);
            let arr = (this.value + '').split('');
            $('#labelPlace' + this.value).text(' Ряд ' + arr[0] + ', Место ' + arr[1]);
        }
    });
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/cinema/hall?id_session=' + session,
        dataType: 'json'
    }).done(function (data) {
        for (let item of data) {
            $('#inputPlace' + item.row + item.cell).prop("disabled", true);
            $('#labelPlace' + item.row + item.cell).text(' Ряд ' + item.row + ', Место ' + item.cell + ' (занято)');
        }
    }).fail(function (err) {
        console.log(err);
    });
}

function goToPayment(){
    let place = $('input[name="place"]:checked').val();
    if (place !== undefined) {
        let s = 'session_id=' + session;
        let arr = place.split('');
        let r = 'row=' + arr[0];
        let c = 'cell=' + arr[1];
        let href = 'http://localhost:8080/cinema/payment.html';
        window.location.href = href + '?' + s + '&' + r + '&' + c;
    } else {
        alert('Выберите место для кинопросмотра!');
    }
}
