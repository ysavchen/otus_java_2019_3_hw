window.onload = function () {
    getDataFromServer();
    document.getElementById("startSection").addEventListener("click", handleStartButton);
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

function handleAddUserButton() {
    fetch('http://localhost:8080/userStore/' + )
        .then(function (response) {
            return response.json();
        })
        .then(function (dataSrv) {
            document.getElementById("infoSection").innerHTML = dataSrv;
        })
}