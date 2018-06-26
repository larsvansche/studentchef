$(document).ready(function() {
    fetch("restservices/recipes")
        .then(function (data) {
            console.log(data);
            return data.json();
        })
        .then(function (recipeObject) {
            console.log(recipeObject);
            for (recipe in recipeObject) {
                console.log(recipeObject[recipe]);

                document.querySelector("#recipes").appendChild(createRecipeBox(recipeObject[recipe]));
            }
        });
});

function createRecipeBox(recipe) {
    var container = document.createElement("a");
    container.setAttribute("href", "/recept.html?recept_id=" + recipe.id);
    container.setAttribute("class", "recipePreview");

    var imageWrapper = document.createElement("div");
    imageWrapper.setAttribute("class", "image");

    var image = document.createElement("img");
    image.setAttribute("src", "http://via.placeholder.com/150x150");
    imageWrapper.appendChild(image);

    container.appendChild(imageWrapper);

    var data = document.createElement("div");
    data.setAttribute("class", "data");

    var recipeTitle = document.createElement("h2");
    recipeTitle.innerText = recipe.title;
    data.appendChild(recipeTitle);

    var recipeParagraph = document.createElement("p");
    recipeParagraph.innerText = recipe.description;
    data.appendChild(recipeParagraph);

    container.appendChild(data);

    return container;
}