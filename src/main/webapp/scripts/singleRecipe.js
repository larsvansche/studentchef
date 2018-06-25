var minuteWidth = 30; // standard pixels width per minute in instructions.

var timerValue = 0;
var timerInterval = null;

var maxDuration = 0;

var userId = 2;

$(document).ready(function () {

    recipeId = getQueryVariable("recept_id");

    document.querySelector("#zoomout").addEventListener("click", zoomOut);
    document.querySelector("#zoomin").addEventListener("click", zoomIn);

    document.querySelector("#moveback").addEventListener("click", timerMoveBack);
    document.querySelector("#startstop").addEventListener("click", timerStartStop);
    document.querySelector("#moveforward").addEventListener("click", timerMoveForward);

    document.querySelector("input#recipeId").value = recipeId;
    document.querySelector("input#userId").value = userId;

    document.querySelector("#ratingForm").addEventListener("submit", function (event) {
        event.preventDefault();
        var formData = new FormData(document.querySelector("#ratingForm"));
        var encData = new URLSearchParams(formData.entries());

        if (document.querySelector("#ratingForm").getAttribute("data-action") == "create") {
            var fetchOptions = {
                method: 'POST',
                body: encData
                // headers : {
                //     'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT")
                // }
            };

            fetch("restservices/ratings/", fetchOptions)
                .then(function (response) {
                    cont = false;
                    if (response.ok) { // response-status = 200 OK
                        console.log("Rating saved!");
                        cont = true;
                        return response.json();
                    } else if (response.status === 401 || response.status === 403) {
                        console.log("User not authorized for operation!");
                        // displayMessage("Je hebt het recht niet om dit te doen!");
                    } else if (response.status === 404) {
                        console.log("Rating not found!");
                        // displayMessage("Land is niet gevonden!");
                    } else {
                        console.log("Cannot save rating!");
                        // displayMessage("De server kon het land niet opslaan!");
                    }
                })
                .then(function (data) {
                    if (!cont) {
                        return;
                    }
                    console.log(data);
                    document.querySelector("#ratingField").appendChild(createRating(data));
                });
        } else {
            var fetchOptions = {
                method: 'PUT',
                body: encData
                // headers : {
                //     'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT")
                // }
            };

            fetch("restservices/ratings/", fetchOptions)
                .then(function (response) {
                    cont = false;
                    if (response.ok) { // response-status = 200 OK
                        console.log("Rating saved!");
                        cont = true;
                        return response.json();
                    } else if (response.status === 401 || response.status === 403) {
                        console.log("User not authorized for operation!");
                        // displayMessage("Je hebt het recht niet om dit te doen!");
                    } else if (response.status === 404) {
                        console.log("Rating not found!");
                        // displayMessage("Land is niet gevonden!");
                    } else {
                        console.log("Cannot save rating!");
                        // displayMessage("De server kon het land niet opslaan!");
                    }
                })
                .then(function (data) {
                    if (!cont) {
                        return;
                    }
                    updateRating(data);

                    document.querySelector("#ratingForm").reset();
                    document.querySelector("#ratingForm").setAttribute("data-action", "create");
                });
        }
    });

    instructionBlock = document.querySelector("#instructions");

    fetch("restservices/recipes/" + recipeId)
        .then(function (data) {
            return data.json();
        })
        .then(function (recipeObject) {
            console.log(recipeObject);
            document.querySelector("h1").innerText = recipeObject.title;
            document.querySelector("#description").innerText = recipeObject.description;
            document.querySelector("#rating").innerHTML = getStarRating(recipeObject.averageRating);
            document.querySelector("#preptime").innerText = "Bereidingstijd: " + recipeObject.preptime + " minuten";

            instructions = JSON.parse(recipeObject.guideJSON);

            for (i in instructions) {
                maxDuration = Math.max(findDuration(instructions[i].steps), maxDuration);
            }

            createInstructions();
            createTimeline();
            changeWidths();
        });

    fetch("restservices/ratings/" + recipeId)
        .then(function (data) {
            return data.json();
        })
        .then(function (ratingObject) {
            console.log(ratingObject);

            var ratingField = document.querySelector("#ratingField");

            for (singleRating in ratingObject) {
                ratingField.appendChild(createRating(ratingObject[singleRating]));
            }
        });
});

function deleteRating() {
    var fetchOptions = {
        method: 'DELETE'
        // headers : {
        //     'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT")
        // }
    };

    fetch("restservices/ratings/" + recipeId + "/" + userId, fetchOptions)
        .then(function (response) {
            cont = false;
            if (response.ok) { // response-status = 200 OK
                console.log("Rating saved!");
                cont = true;
            } else if (response.status === 401 || response.status === 403) {
                console.log("User not authorized for operation!");
                // displayMessage("Je hebt het recht niet om dit te doen!");
            } else if (response.status === 404) {
                console.log("Rating not found!");
                // displayMessage("Land is niet gevonden!");
            } else {
                console.log("Cannot save rating!");
                // displayMessage("De server kon het land niet opslaan!");
            }
        })
        .then(function () {
            if (!cont) {
                return;
            }
            document.querySelector(".owned").outerHTML = "";
        });
}

function editRating() {
    fetch("restservices/ratings/" + recipeId + "/" + userId)
        .then(function (response) {
            cont = false;
            if (response.ok) { // response-status = 200 OK
                console.log("Rating saved!");
                cont = true;
                return response.json();
            } else if (response.status === 401 || response.status === 403) {
                console.log("User not authorized for operation!");
                // displayMessage("Je hebt het recht niet om dit te doen!");
            } else if (response.status === 404) {
                console.log("Rating not found!");
                // displayMessage("Land is niet gevonden!");
            } else {
                console.log("Cannot save rating!");
                // displayMessage("De server kon het land niet opslaan!");
            }
        })
        .then(function (data) {
            if (!cont) {
                return;
            }
            console.log(data);
            document.querySelector("#userId").value = data.userId;
            document.querySelector("#recipeId").value = data.recipeId;
            document.querySelector("#value").value = data.value;
            document.querySelector("#recipeDescription").value = data.description;
            document.querySelector("#ratingForm").setAttribute("data-action", "update");
        });
}

function updateRating(singleRating) {
    console.log(singleRating);
    var rating = document.querySelector("div.owned");

    var username = rating.querySelector("h2.username");
    username.innerText = singleRating.username;

    var stars = rating.querySelector("div.stars");
    stars.innerHTML = getStarRating(singleRating.value);

    if (singleRating.description !== null || singleRating.description !== "") {
        var description = rating.querySelector("p.description");
        description.innerText = singleRating.description;
    }

    var datetime = rating.querySelector("p.datetime");
    var unix = new Date(singleRating.datetime * 1000);
    datetime.innerHTML = unix.getDate() + "/" + unix.getMonth() + "/" + unix.getFullYear() + " " + unix.getHours() + ":" + unix.getMinutes();
}

function createRating(singleRating) {
    console.log(singleRating);
    var rating = document.createElement("div");
    rating.classList.add("rating");

    var img = document.createElement("img");
    img.src = "http://via.placeholder.com/150x150";
    rating.appendChild(img);

    var ratingData = document.createElement("div");
    ratingData.classList.add("ratingData");

    if (userId = singleRating.userId) {
        rating.classList.add("owned");

        var editButton = document.createElement("button");
        editButton.innerText = "Wijzigen";
        editButton.addEventListener("click", editRating);
        ratingData.appendChild(editButton);
    }

    var deleteButton = document.createElement("button");
    deleteButton.innerText = "Verwijderen";
    deleteButton.addEventListener("click", deleteRating);
    ratingData.appendChild(deleteButton);

    var username = document.createElement("h2");
    username.classList.add("username");
    username.innerText = singleRating.username;
    ratingData.appendChild(username);

    var stars = document.createElement("div");
    stars.classList.add("stars");
    stars.innerHTML = getStarRating(singleRating.value);
    ratingData.appendChild(stars);

    if (singleRating.description !== null || singleRating.description !== "") {
        var description = document.createElement("p");
        description.classList.add("description");
        description.innerText = singleRating.description;
        ratingData.appendChild(description);
    }

    var datetime = document.createElement("p");
    datetime.classList.add("datetime");
    var unix = new Date(singleRating.datetime * 1000);
    datetime.innerHTML = unix.getDate() + "/" + unix.getMonth() + "/" + unix.getFullYear() + " " + unix.getHours() + ":" + unix.getMinutes();
    ratingData.appendChild(datetime);

    rating.appendChild(ratingData);

    return rating;
}

function zoomIn() {
    minuteWidth = minuteWidth * 1.075;
    changeWidths();
}

function zoomOut() {
    minuteWidth = minuteWidth / 1.075;
    changeWidths();
}

function timerMoveBack() {
    if ((timerValue - 10) <= 0) {
        timerValue = 0;
    } else {
        timerValue -= 10;
    }
    updateTimerIndicator();
}

function timerMoveForward() {
    if ((timerValue + 10) >= maxDuration * 60) {
        timerValue = maxDuration * 60;
    } else {
        timerValue += 10;
    }
    updateTimerIndicator();
}

function timerStartStop() {
    if (timerInterval !== null) {
        clearInterval(timerInterval);
        timerInterval = null;
        return;
    }
    timerInterval = setInterval(function () {
        timerValue += 1;
        updateTimerIndicator();
    }, 1000);
}

function updateTimerIndicator() {
    if ((timerValue + 1) >= maxDuration * 60) {
        timerValue = maxDuration * 60;
        clearInterval(timerInterval);
    }

    timerClock = document.querySelector("#timerClock");
    var hours = Math.floor(timerValue / 60 / 60);
    var minutes = Math.floor((timerValue - hours * 60) / 60);
    var seconds = timerValue % 60;
    hours = hours < 10 ? "0" + hours : hours;
    minutes = minutes < 10 ? "0" + minutes : minutes;
    seconds = seconds < 10 ? "0" + seconds : seconds;
    timerClock.innerText = hours + ":" + minutes + ":" + seconds;

    timerIndicator = document.querySelector("#indicator");
    timerIndicator.style.left = Math.floor(minuteWidth / 60 * timerValue) + 120 + "px";
}

function changeWidths() {
    var steps = document.querySelectorAll(".step, .offset, .timelineSegment");
    for (var i = 0; i < steps.length; i++) {
        var stepWidth = steps[i].getAttribute("data-duration");
        steps[i].style.minWidth = Math.floor(minuteWidth * stepWidth) + "px";
        steps[i].style.maxWidth = Math.floor(minuteWidth * stepWidth) + "px";
    }
    updateTimerIndicator();
}

function findDuration(steps) {
    var duration = 0;

    for (j in steps) {
        duration += steps[j].duration;
    }

    return duration;
}

function createTimeline() {

    var timelineRow = document.querySelector("#timeline tr");

    for (var i = 0; i <= maxDuration; i += minuteWidth / 10) {
        var cell = document.createElement("td");
        cell.classList.add("timelineSegment");
        cell.setAttribute("data-duration", minuteWidth / 10);
        cell.innerText = i;
        cell.style.width = minuteWidth * 10 + "px";
        timelineRow.appendChild(cell);
    }
}

function createInstructions() {
    for (i in instructions) {
        var row = createRow(instructions[i].name, instructions[i].amount, instructions[i].unit_abbr);

        var steps = instructions[i].steps;
        for (j in steps) {
            createStep(steps[j], row);
        }
    }
}

function createRow(name, amount, unit) {
    var table = document.createElement("table");
    table.classList.add("ingredient");

    var row = document.createElement("tr");

    var cell = document.createElement("td");
    cell.classList.add("name");

    var nameElement = document.createElement("p");
    nameElement.innerText = name;

    var amountElement = document.createElement("p");
    amountElement.innerText = amount + " " + unit;

    cell.appendChild(nameElement);
    cell.appendChild(amountElement);

    row.appendChild(cell);
    table.appendChild(row);
    instructionBlock.appendChild(table);
    return row;
}

function createStep(step, row) {
    if (step.offset !== 0) {
        var offset = document.createElement("td");
        offset.classList.add("offset");
        offset.setAttribute("data-duration", step.offset);
        offset.innerHTML = "&nbsp;";
        row.appendChild(offset);
    }

    var element = document.createElement("td");
    element.classList.add("step");
    element.innerText = step.short_desc;
    element.setAttribute("data-duration", step.duration);
    element.setAttribute("data-long_desc", step.long_desc);
    element.addEventListener("click", showDescription);
    row.appendChild(element);
}

function showDescription(event) {
    var element = event.target;
    var data = element.getAttribute("data-long_desc");
    var activeStep = document.querySelector(".activeStep");

    if (activeStep !== null) {
        activeStep.classList.remove("activeStep");
    }
    element.classList.add("activeStep");
    document.querySelector("#descriptionField").innerText = data;
}

function getStarRating(rating) {
    var htmlString = "";

    if (rating === -1) {
        return "Er zijn nog geen beoordelingen!";
    }

    for (i = 0; i < 5; i++) {
        if (i < rating) {
            htmlString += "<i class='fas fa-star'></i>";
        } else {
            htmlString += "<i class='far fa-star'></i>";
        }
    }

    return htmlString;
}
