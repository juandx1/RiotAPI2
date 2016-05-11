var finded = false;

function executeApp(){

    var summoner = $('#summoner-input').val();
    var region = $('#region-input').val();

    if(summoner == "" || region == ""){
        $('#error-message').fadeIn();
    }else{
        getPlayerData(summoner, region);
        setTopChamps();
    }
}

function executeApp2(){

    var summoner = $('#summoner-input2').val();
    var region = $('#region-input2').val();

    if(summoner == "" || region == ""){
        $('#error-message1').fadeIn();
    }else{
        getPlayerData(summoner, region);
        setTopChamps();
    }
}

var userchamps;

function getPlayerData(summonerName, platformId){
    $.ajax({
        url: '/getPlayerData',
        type: 'GET',
        dataType: 'json',
        data: {summonerName: summonerName, platformId: platformId}
    })
    .done(function(json) {

        finded = true;

        if(json.champions[0] == undefined){
            $('#error-message').text("You haven't played enough ranked games this season or you haven't mastered any champion yet :(");
            $('#error-message').fadeIn();
            $('#error-message1').text("You haven't played enough ranked games this season or you haven't mastered any champion yet :(");
            $('#error-message1').fadeIn();
        }else{
            $('#error-message1').hide();
            userchamps = json.champions;

            var champions = json.champions;

            $('.user-champions').empty();

            for(var key in champions){
                var champion = champions[key];
                $('.user-champions').append('<div class="div-flexed"><a href="#" onclick="selectUserChampion('+key+', '+champion.championId+')"><div class="champion"><div class="champ-img" id="img-user-'+key+'"></div><div class="champ-name" id="name-user-1">'+champion.championName+'</div></div></a></div>');
                $('#img-user-'+key).css({
                    background: 'url("'+champion.championSquareUrl+'")',
                    "background-position": "center",
                    "background-size": "80px 80px"
                });
            }

            selectUserChampion(0,champions[0].championId);

            $('#summoner-input2').val(summonerName);

            $('#welcome').fadeOut(500);
            $('#first-container').delay(500).fadeIn(500);
            $('#top-bar').delay(500).fadeIn(500);
            $('#logo').css({top: "10px", bottom: "initial"});
            $('#logo').hide().delay(500).fadeIn(500);
            $('.divider').show();
            $('#section2').show();

            $("#song").prop("currentTime", 51);
        }
    })
    .fail(function(error) {
        $('#error-message').text('Summoner not found');
        $('#error-message').fadeIn();
        $('#error-message1').text('Summoner not found');
        $('#error-message1').fadeIn();
    })
    .always(function() {
        console.log("complete");
    });
    
}

function selectUserChampion(key, championId){
    event.preventDefault();

    var champ = userchamps[key];
    var wrate = ((champ.games-champ.losses)/champ.games).toFixed(2);
    $('#kills').text((champ.kills/champ.games).toFixed(2));
    $('#deaths').text((champ.deaths/champ.games).toFixed(2));
    $('#assists').text((champ.assits/champ.games).toFixed(2));

    $('#wl').text((wrate*100).toFixed(1)+"%");
    $('#games').text(champ.games);
    $('#gold').text((champ.gold/(champ.games*1000)).toFixed(2)+"k");

    $('#user-champion-name').text(champ.championName);
    $('#user-champion-square').attr('src', champ.championSquareUrl);
    $('#user-champion-bg').css({
        background: 'url("'+champ.championSplashUrl+'")',
        "background-position": "center center",
        "background-repeat": "no-repeat",
        "background-size": "cover"
    });

    $('#suggestion').empty();
    if(champ.rol == "top"){
        $('#suggestion').append('Based on this champion&#39;s rol we suggest you to watch the following streamer: <a target="_blank" href="https://www.twitch.tv/tsm_dyrus">Dyrus</a>');
    }else if(champ.rol == "jungle"){
        $('#suggestion').append('Based on this champion&#39;s rol we suggest you to watch the following streamer: <a target="_blank" href="https://www.twitch.tv/nightblue3">NightBlue3</a>');
    }else if(champ.rol == "mid"){
        $('#suggestion').append('Based on this champion&#39;s rol we suggest you to watch the following streamer: <a target="_blank" href="https://www.twitch.tv/tsm_bjergsen">Bjergsen</a>');
    }else if(champ.rol == "adc"){
        $('#suggestion').append('Based on this champion&#39;s rol we suggest you to watch the following streamer: <a target="_blank" href="https://www.twitch.tv/imaqtpie">Imaqtpie</a>');
    }else if(champ.rol == "support"){
        $('#suggestion').append('Based on this champion&#39;s rol we suggest you to watch the following streamer: <a target="_blank" href="https://www.twitch.tv/aphromoo">Aphromoo</a>');
    }

    $.ajax({
        url: '/getBestWorld',
        type: 'GET',
        dataType: 'json',
        data: {championId: championId}
    })
    .done(function(champ1) {
        var wrate1 = ((champ1.games-champ1.lost)/champ1.games).toFixed(2);

        var d1 = (((champ.kills/champ.games)*10)/(champ1.kills/champ1.games)).toFixed(2);
        var d2 = (100/((champ.deaths/champ.games)*10)/(champ1.deaths/champ1.games)).toFixed(2);
        var d3 = (((champ.assits/champ.games)*10)/(champ1.assits/champ1.games)).toFixed(2);
        var d4 = ((wrate*10)/wrate1).toFixed(2);
        var d5 = (((champ.gold/champ.games)*10)/(champ1.gold/champ1.games)).toFixed(2);
        var d6 = ((champ.games*10)/champ1.games).toFixed(2);

        generateRadarChart([d1,d2,d3,d4,d5,d6]);

        $('#kills1').text((champ1.kills/champ1.games).toFixed(2));
        $('#deaths1').text((champ1.deaths/champ1.games).toFixed(2));
        $('#assists1').text((champ1.assits/champ1.games).toFixed(2));

        $('#wl1').text((wrate1*100).toFixed(1)+"%");
        $('#games1').text(champ1.games);
        $('#gold1').text((champ1.gold/(champ1.games*1000)).toFixed(2)+"k");
    })
    .fail(function() {
        console.log("error");
    })
    .always(function() {
        console.log("complete");
    });
    
}

var topChamps;

function setTopChamps(){
    $.ajax({
        url: '/getChampionProbabilityRegion',
        type: 'GET',
        dataType: 'json'
    })
    .done(function(json) {
        topChamps = json;

        $('#a-top-1').attr('onclick', 'selectTopChampion(0, "'+json[0].championName+'")');
        $('#img-top-1').css({
            background: 'url("'+json[0].championSquareUrl+'")',
            backgroundPosition: 'center',
            backgroundSize: '80px 80px'
        });
        $('#name-top-1').text(json[0].championName);

        $('#a-top-2').attr('onclick', 'selectTopChampion(1, "'+json[1].championName+'")');
        $('#img-top-2').css({
            background: 'url("'+json[1].championSquareUrl+'")',
            backgroundPosition: 'center',
            backgroundSize: '80px 80px'
        });
        $('#name-top-2').text(json[1].championName);

        $('#a-top-3').attr('onclick', 'selectTopChampion(2, "'+json[2].championName+'")');
        $('#img-top-3').css({
            background: 'url("'+json[2].championSquareUrl+'")',
            backgroundPosition: 'center',
            backgroundSize: '80px 80px'
        });
        $('#name-top-3').text(json[2].championName);

        $('#a-top-4').attr('onclick', 'selectTopChampion(3, "'+json[3].championName+'")');
        $('#img-top-4').css({
            background: 'url("'+json[3].championSquareUrl+'")',
            backgroundPosition: 'center',
            backgroundSize: '80px 80px'
        });
        $('#name-top-4').text(json[3].championName);

        $('#a-top-5').attr('onclick', 'selectTopChampion(4, "'+json[4].championName+'")');
        $('#img-top-5').css({
            background: 'url("'+json[4].championSquareUrl+'")',
            backgroundPosition: 'center',
            backgroundSize: '80px 80px'
        });
        $('#name-top-5').text(json[4].championName);

        selectTopChampion(0, json[0].championName);
    })
    .fail(function() {
        console.log("error");
    })
    .always(function() {
        console.log("complete");
    });
}

function selectTopChampion(id, championName){
    event.preventDefault();
    generateChart([topChamps[id].brProbability.toFixed(2), topChamps[id].euneProbability.toFixed(2), topChamps[id].euwProbability.toFixed(2), topChamps[id].jpProbability.toFixed(2), topChamps[id].krProbability.toFixed(2), topChamps[id].lanProbability.toFixed(2), topChamps[id].lasProbability.toFixed(2), topChamps[id].naProbability.toFixed(2), topChamps[id].oceProbability.toFixed(2), topChamps[id].trProbability.toFixed(2), topChamps[id].ruProbability.toFixed(2), topChamps[id].worldProbability.toFixed(2)], championName);
}

function generateChart(info, championName){

    $('#myChart').remove();
    $('.chart-container').append('<canvas id="myChart" width="900" height="510"></canvas>');

    var ctx = $("#myChart");

    var myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ["BR", "EUNE", "EUW", "JP", "KR", "LAN", "LAS", "NA", "OCE", "TR", "RU", "WORLD"],
            datasets: [{
                label: 'Percentage of players that have level 5 mastery',
                backgroundColor: "rgba(240,216,149,0.2)",
                borderColor: "rgba(240,216,149,1)",
                borderWidth: 1,
                hoverBackgroundColor: "rgba(240,216,149,0.4)",
                hoverBorderColor: "rgba(240,216,149,1)",
                data: info
            }]
        },
        options: {
            maintainAspectRatio: true,
            responsive: false,
            legend: {
                display: false
            },
            scales: {
                yAxes: [{
                    gridLines: {
                        color: "rgba(255,255,255,.1)"
                    },
                    ticks: {
                        beginAtZero:true,
                        fontColor: "#fff"
                    }
                }],
                xAxes: [{
                    gridLines: {
                        color: "rgba(255,255,255,.1)"
                    },
                    ticks: {
                        fontColor: "#fff"
                    }
                }]
            },
            title: {
                display: true,
                text: 'Stats for '+championName,
                fontColor: "#f0d895",
                fontSize: 16,
                fontFamily: 'Open Sans',
                fontStyle: '600'
            }
        }
    });
}

function generateRadarChart(info){

    var data = {
        labels: ["Kills", "Deaths", "Assists", "W/L", "Gold", "Games"],
        datasets: [
        {
            label: "Your Stats",
            backgroundColor: "rgba(179,181,198,0.2)",
            borderColor: "rgba(179,181,198,1)",
            pointBackgroundColor: "rgba(179,181,198,1)",
            pointBorderColor: "#fff",
            pointHoverBackgroundColor: "#fff",
            pointHoverBorderColor: "rgba(179,181,198,1)",
            data: info
        },
        {
            label: "World's Best",
            backgroundColor: "rgba(244,160,47,0.2)",
            borderColor: "rgba(244,160,47,1)",
            pointBackgroundColor: "rgba(244,160,47,1)",
            pointBorderColor: "#fff",
            pointHoverBackgroundColor: "#fff",
            pointHoverBorderColor: "rgba(244,160,47,1)",
            data: [10, 10, 10, 10, 10, 10]
        }
        ]
    };

    $('#myChart1').remove();
    $('#radarChart').append('<canvas id="myChart1" width="280" height="280"></canvas>');

    var ctx = $("#myChart1");

    var myChart = new Chart(ctx, {
        type: 'radar',
        data: data,
        options: {
            maintainAspectRatio: true,
            responsive: false,
            legend: {
                position: 'bottom',
                labels: {
                    fontColor: "#f4a02f"
                }
            },
            title: {
                display: true,
                text: "Your performance compared to world's best",
                fontColor: "#f4a02f"
            },
            scale: {
                ticks: {
                    display: false,
                    beginAtZero: true,
                    showLabelBackdrop: false
                },
                pointLabels: {
                    fontColor: "#f0f8ff",
                }
            }
        }
    });

}

function displayAbout(){
    event.preventDefault();
    $('#section1').fadeOut();
    $('#section2').fadeOut();
    $('#top-bar').fadeOut();
    $('.divider').fadeOut();
    $('#section3').delay(1000).fadeIn();
    $('#a-about').fadeOut();

    if(finded){
        $('#a-home1').delay(1000).fadeIn();
    }else{
        $('#a-home').delay(1000).fadeIn();
    }

    var x = document.getElementById("song");
    var x1 = document.getElementById("song1");
    $("#song1").prop("currentTime", 0);
    x1.volume = 0;
    x1.loop = true;
    x1.play();

    setTimeout(function() {
        x.volume = 0.15;
    }, 200);
    setTimeout(function() {
        x.volume = 0.1;
        x1.volume = 0.1;
    }, 400);
    setTimeout(function() {
        x.volume = 0.05;
        x1.volume = 0.2;
    }, 600);
    setTimeout(function() {
        x.volume = 0.025;
        x1.volume = 0.3;
        x.pause();
    }, 800);
}

function displayHome(){
    event.preventDefault();
    $('#section3').fadeOut();
    $('#section1').delay(1000).fadeIn();
    $('#a-home').fadeOut();
    $('#a-about').delay(1000).fadeIn();

    var x1 = document.getElementById("song");
    var x = document.getElementById("song1");
    $("#song").prop("currentTime", 0);
    x1.volume = 0;
    x1.loop = true;
    x1.play();

    setTimeout(function() {
        x.volume = 0.15;
    }, 200);
    setTimeout(function() {
        x.volume = 0.1;
        x1.volume = 0.1;
    }, 400);
    setTimeout(function() {
        x.volume = 0.05;
        x1.volume = 0.2;
    }, 600);
    setTimeout(function() {
        x.volume = 0.025;
        x1.volume = 0.3;
        x.pause();
    }, 800);
}

function displayHome2(){
    event.preventDefault();
    $('#section3').fadeOut();
    $('#a-home1').fadeOut();
    $('#top-bar').delay(1000).fadeIn();
    $('.divider').delay(1000).fadeIn();
    $('#section1').delay(1000).fadeIn();
    $('#section2').delay(1000).fadeIn();
    $('#a-about').delay(1000).fadeIn();

    var x1 = document.getElementById("song");
    var x = document.getElementById("song1");
    $("#song").prop("currentTime", 0);
    x1.volume = 0;
    x1.loop = true;
    x1.play();

    setTimeout(function() {
        x.volume = 0.15;
    }, 200);
    setTimeout(function() {
        x.volume = 0.1;
        x1.volume = 0.1;
    }, 400);
    setTimeout(function() {
        x.volume = 0.05;
        x1.volume = 0.2;
    }, 600);
    setTimeout(function() {
        x.volume = 0.025;
        x1.volume = 0.3;
        x.pause();
    }, 800);
}

$(function() {
    $('#first-container').hide();
    $('#top-bar').hide();
    $('#error-message').hide();
    $('#error-message1').hide();
    $('.divider').hide();
    $('#section2').hide();
    $('#section3').hide();
    $('#a-home').hide();
    $('#a-home1').hide();

    var x = document.getElementById("song");
    x.volume = 0.3;
    x.loop = true;
    x.play();

    $('.ui.dropdown').dropdown({
        fullTextSearch:true
    });
});