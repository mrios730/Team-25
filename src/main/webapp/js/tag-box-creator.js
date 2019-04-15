function fetchTags(){
  const url = "/event-tags";
  fetch(url).then((response) => {
    return response.json();
  }).then((tags) => {
    const tagContainer = document.getElementById('tag-container');
    if(tags.length == 0){
     tagContainer.innerHTML = '<p>There are no tags at this time. \
     Please check again later</p>';
    }
    else{
     tagContainer.innerHTML = '';  
    }
    tags.forEach((tag) => {
     //const tagDiv = buildTagDiv(tag);
     tagContainer.appendChild(createListItem(createLink('/event-list.html?tags=' + tag, tag)));
    });
  });
}

function buildTagBox() {
  fetchTags();
}