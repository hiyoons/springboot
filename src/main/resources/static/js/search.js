function selectElement(selectedElement) {
    addDataToForm(selectedElement);
    window.close();
}

// 선택된 데이터를 add form 에 추가하는 함수
function addDataToForm(ele) {
    console.log('addDataToForm' + ele);

    let elements = ele.getElementsByTagName("td");

    let data = {
        title: elements[1].innerText,
        author: elements[2].innerText,
        imageURL: elements[0].querySelector('img').getAttribute('src')
    };




    // 부모 창으로 element 전달
    opener.document.getElementById("add_form_imageURL").value = data.imageURL;
    opener.document.getElementById("add_form_title").value = data.title;
    opener.document.getElementById("add_form_author").value = data.author;

}