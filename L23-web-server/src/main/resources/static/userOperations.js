window.onload = function () {
    getDataFromServer();
}

function getDataFromServer() {
    fetch('http://localhost:8080/userData/' + new Date())
        .then(function (response) {
            return response.json();
        })
        .then(function (dataSrv) {
            document.getElementById("dataHolder").innerHTML = dataSrv;
        })
}