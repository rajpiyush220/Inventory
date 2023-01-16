function sortChanged(selectedColumn){
    sortOrder = (sortOrder === 'ASC') ? 'DESC' : 'ASC';
    sortColumn = selectedColumn;
    location.href = build_local_sort_url('details',currentPageSize,currentPageNumber,sortOrder,sortColumn,searchDate);
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
    location.href = build_local_sort_url('details',currentPageSize,currentPageNumber,sortOrder,sortColumn,searchDate);
}

function submitPageSizeChange(){
    currentPageSize = $('#pageSize').val();
    location.href = build_local_sort_url('details',currentPageSize,currentPageNumber,sortOrder,sortColumn,searchDate);
}

function clearDataFilter(){
    location.href = build_local_sort_url('details',0,0,'','',searchDate);
}

function searchBySuggestion(){
    searchDate = $('#dateSelect').val()
    location.href = build_local_sort_url('details',currentPageSize,currentPageNumber,sortOrder,sortColumn,searchDate);
}

function build_local_sort_url(endpoint,size,number,order,column,searchDate){
    let url = getBasePath().concat("/").concat(endpoint);
    if(size > 0 || number > 0 || order.length > 0 || column.length > 0 || searchDate.length > 0){
        url = url.concat("?");
    }
    if(size > 0){
        url = url.concat("&size=").concat(size);
    }
    if(number > 0){
        url = url.concat("&page=").concat(number);
    }
    if(order.length > 0){
        url = url.concat("&sortOrder=").concat(order);
    }
    if(column.length > 0){
        url = url.concat("&sortColumn=").concat(column);
    }
    if(searchDate.length > 0){
        url = url.concat("&searchDate=").concat(searchDate);
    }
    return url;
}