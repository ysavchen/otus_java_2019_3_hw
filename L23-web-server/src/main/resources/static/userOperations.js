window.onload = function () {
    getDataFromServer();
    document.getElementById("addUserButton").addEventListener("click", handleAddUserButton);
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
    let name = document.getElementById("name").value;
    let age = document.getElementById("age").value;
    let surname = document.getElementById("surname").value;

    if(!Number.isInteger(age)) {
        age = 0;
    }

    const userStoreUrl = "http://localhost:8080/userStore";
    let user = { name: document.getElementById("name").value,
                 surname: document.getElementById("surname").value,
                 age: document.getElementById("age").value
               };

    fetch(userStoreUrl, {
        method: 'POST',
        body: JSON.stringify(user),
          headers:{
            'Content-Type': 'application/json'
          }
        })
        .then(function (response) {
            return response.json();
         })
        .then(function (dataSrv) {
            document.getElementById("infoSection").innerHTML = dataSrv;
        })
        .then(function () {
            document.getElementById("name").value = "";
            document.getElementById("age").value = "";
            document.getElementById("surname").value = "";
        })
}