window.onload = function () {
    document.getElementById("addUserButton").addEventListener("click", handleAddUserButton);
    document.getElementById("allUsersButton").addEventListener("click", handleAllUsersButton);
}

function handleAddUserButton() {
    let name = document.getElementById("name").value;
    let age = document.getElementById("age").value;
    let surname = document.getElementById("surname").value;

    if(!Number.isInteger(age)) {
        age = 0;
    }

    const userStoreUrl = "http://localhost:8080/userStore";
    let user = { name: name, surname: surname, age: age };

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

function handleAllUsersButton() {
    fetch('http://localhost:8080/allUsersData')
            .then(function (response) {
                return response.json();
            })
            .then(function (dataSrv) {
                //todo: [object Object],[object Object]
                document.getElementById("dataHolder").innerHTML = dataSrv;
            })
}