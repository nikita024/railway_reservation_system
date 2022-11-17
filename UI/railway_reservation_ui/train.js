$(document).ready(function(){
    var button = document.getElementsByClassName("button");
    var trainDetails = document.getElementsByClassName("frame4");
    $('#formSubmit').click(function(){
        console.log("inside submit form")
        var formData = {
            sourceStationId : $("#FromPlace").val(),
            destinationStationId : $("#ToPlace").val(),
            date : $("Dateofjourney").val()
        }
        console.log(formData);
        $.ajax({
            type: "POST",
            url: "localhost:8081/railway_reservation_system/ticketAvailabilityServlet",
            crossDomain : true,
            headers: {
                'Access-Control-Allow-Origin': '*',
            },
            data: formData,
            dataType: "json",
            //encode: true,
            success : function(data){
                console.log(data);
            }
          })
      
          event.preventDefault();
    }) 
});