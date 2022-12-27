function sortChanged(selectedColumn){
    sortOrder = (sortOrder === 'ASC') ? 'DESC' : 'ASC';
    sortColumn = selectedColumn;
    location.href = build_sort_url('categories',currentPageSize,currentPageNumber,sortOrder,sortColumn,searchKey,searchType);
}

function paginationClicked(index,pagenumber){
    index = parseInt(index);
    if (index > 0) {
      currentPageNumber = parseInt(currentPageNumber) + 1
    } else if (index < 0) {
      currentPageNumber = parseInt(currentPageNumber) - 1
    }else{
        currentPageNumber = parseInt(pagenumber);
    }
    location.href = build_sort_url('categories',currentPageSize,currentPageNumber,sortOrder,sortColumn,searchKey,searchType);
}

function submitPageSizeChange(){
    currentPageSize = $('#pageSize').val();
    location.href = build_sort_url('categories',currentPageSize,currentPageNumber,sortOrder,sortColumn,searchKey,searchType);
}

function clearDataFilter(){
    location.href = build_sort_url('categories',0,0,'','','','');
}

function searchBySuggestion(){
    searchKey = $('#searchKey').val();
    searchType = $('#searchType').val()
    location.href = build_sort_url('categories',currentPageSize,currentPageNumber,sortOrder,sortColumn,searchKey,searchType);
}

function searchTypeChanged(){
    var searchType = $('#searchType').val();
    $('#searchKey').devbridgeAutocomplete().setOptions({
        params: {type:searchType}
    });
}