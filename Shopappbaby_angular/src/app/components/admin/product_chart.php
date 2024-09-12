<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">  
  <link rel="icon" type="image/x-icon" href="favicon.ico">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  
  <title>Thống kê Sản Phẩm</title>
</head>
<body>
  <div class="container">

  <?php 
    $con = new mysqli('localhost','root','','shopappbaby');
    $query = $con->query("
     SELECT name AS name, price AS price 
    FROM products 
    WHERE id BETWEEN 1 AND 12
    ");
  
    foreach($query as $data)
    {
      $name[] = $data['name'];
      $price[] = $data['price'];
    }
  
  ?>
  
  
  <div style="width: 1000px;height:800px;margin-left:230px;margin-top:80px;">
    <canvas id="myChart"></canvas>
  </div>
   
  <script>
    // === include 'setup' then 'config' above ===
    const labels = <?php echo json_encode($name) ?>;
    const data = {
      labels: labels,
      datasets: [{
        label: 'Biểu Đồ Sản Phẩm',
        data: <?php echo json_encode($price) ?>,
        backgroundColor: [
          'rgba(255, 99, 132, 0.2)',
          'rgba(255, 159, 64, 0.2)',
          'rgba(255, 205, 86, 0.2)',
          'rgba(75, 192, 192, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(153, 102, 255, 0.2)',
          'rgba(201, 203, 207, 0.2)'
        ],
        borderColor: [
          'rgb(255, 99, 132)',
          'rgb(255, 159, 64)',
          'rgb(255, 205, 86)',
          'rgb(75, 192, 192)',
          'rgb(54, 162, 235)',
          'rgb(153, 102, 255)',
          'rgb(201, 203, 207)'
        ],
        borderWidth: 1
      }]
    };
  
    const config = {
      type: 'bar',
      data: data,
      options: {
        scales: {
          y: {
            beginAtZero: true
          }
        }
      },
    };
  
    var myChart = new Chart(
      document.getElementById('myChart'),
      config
    );
  </script>
  </div>


</body>
</html>