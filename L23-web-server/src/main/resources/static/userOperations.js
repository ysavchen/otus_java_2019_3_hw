const userStoreUrl = "http://localhost:8080/userStore";
const allUsersDataUrl = "http://localhost:8080/userData";

window.onload = function () {
    document.getElementById("addUserButton").addEventListener("click", handleAddUserButton);
    document.getElementById("userDataButton").addEventListener("click", handleAllUsersButton);
}

function handleAddUserButton() {
    let name = document.getElementById("name").value;
    let surname = document.getElementById("surname").value;
    let age = document.getElementById("age").value;

    if(!Number.isInteger(parseInt(age, 10))) {
        showInfoMessage("Invalid value for age: " + age);
        return;
    }

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
        .then(function (message) {
            showInfoMessage(message);
        });
}

function showInfoMessage(message) {
    let p = document.createElement("p")
    p.textContent = message;
    let info = document.getElementById("infoSection");
    info.innerHTML = "";
    info.appendChild(p);
}

function handleAllUsersButton() {
    fetch(allUsersDataUrl)
            .then(function (response) {
                return response.json();
            })
            .then(function (dataSrv) {
                let table = document.createElement("table");
                let head = document.createElement("thead");

                let columns = document.createElement("tr");
                let nameColumn = document.createElement("td");
                nameColumn.textContent = "Name";
                let surnameColumn = document.createElement("td");
                surnameColumn.textContent = "Surname";
                let ageColumn = document.createElement("td");
                ageColumn.textContent = "Age";
                let body = document.createElement("tbody");

                for (let user of dataSrv) {
                    let row = document.createElement("tr");
                    let nameData = document.createElement("td");
                    nameData.textContent = user.name;

                    let surnameData = document.createElement("td");
                    surnameData.textContent = user.surname;

                    let ageData = document.createElement("td");
                    ageData.textContent = user.age;

                    row.appendChild(nameData);
                    row.appendChild(surnameData);
                    row.appendChild(ageData);
                    body.appendChild(row);
                }

                columns.appendChild(nameColumn);
                columns.appendChild(surnameColumn);
                columns.appendChild(ageColumn);
                head.appendChild(columns);
                table.appendChild(head);
                table.appendChild(body);

                const holder = document.getElementById("dataHolder");
                holder.innerHTML = "";
                holder.appendChild(table);
            });
}