let formEdit = document.forms["formEditUser"];
editUser();

async function editModalData(id) {
    const modal = new bootstrap.Modal(document.querySelector('#editModal'));
    await theModal(formEdit, modal, id);
    loadRoles();
}

function editUser() {
    formEdit.addEventListener("submit", ev => {
        ev.preventDefault();
        let editUserRoles = [];
        for (let i = 0; i < formEdit.roles.options.length; i++) {
            if (formEdit.roles.options[i].selected) editUserRoles.push({
                id: formEdit.roles.options[i].value,
                role: formEdit.roles.options[i].text
            });
        }
        fetch("http://localhost:8080/admin/" + formEdit.id.value, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: formEdit.id.value,
                firstName: formEdit.firstName.value,
                secondName: formEdit.secondName.value,
                age: formEdit.age.value,
                username: formEdit.username.value,
                password: formEdit.password.value,
                userRoles: editUserRoles
            })
        }).then(response => {
            if (response.ok) {
                $('#editFormCloseButton').click();
                tableOfAllUsers();
            } else {
                response.json().then(errors => {
                    displayEditErrors(errors);
                });
            }
        });
    });
}



function displayEditErrors(errors) {
    try {
        let errorEditDiv = document.getElementById("errorEditDiv");
        errorEditDiv.innerHTML = "";
        errors.forEach(error => {
            let errorSpan = document.createElement("span");
            errorSpan.className = "error-message";
            errorSpan.innerHTML = error;
            errorEditDiv.appendChild(errorSpan);
        });
    } catch (e) {
        if (e instanceof TypeError) {
            $('#editFormCloseButton').click();
            tableOfAllUsers();
        }
    }
}

function loadRoles() {
    let select = document.getElementById("roleEdit");
    select.innerHTML = "";

    fetch("http://localhost:8080/admin/roles")
        .then(res => res.json())
        .then(data => {
            data.forEach(role => {
                let option = document.createElement("option");
                option.value = role.id;
                option.text = role.roleName;
                select.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}

window.addEventListener("load", loadRoles);
