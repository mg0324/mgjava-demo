mg = {};

//项目跟路径 不带项目名的
mg.root = function(){  
    //url http://ip:8080/
    var url=window.document.location.href;
    var a = window.document.location.pathname;  
    alert(a);
    var p = url.split("/");
    return p[0]+"//"+p[2];
}
//带一级名
mg.rootpath = function(){
    var curWwwPath=window.document.location.href;  
    var pathName=window.document.location.pathname;  
    var pos=curWwwPath.indexOf(pathName);  
    var localhostPaht=curWwwPath.substring(0,pos);  
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);  
    return(localhostPaht+projectName);  
}
