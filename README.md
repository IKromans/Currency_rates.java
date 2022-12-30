# Currency_rates.java

Hey!

Welcome to my application that reads currency rates from `https://www.bank.lv/vk/ecb_rss.xml`, saves them to database and displays from API.

Code is in Java with Maven build tool and Javalin library for two endpoints.

The user is able to input commands to either start endpoints or fetch data.

In order to be able to run application from your computer. Java Development Kit (JDK) and Apache Maven should be installed and configured.
Also you need some relational database management system on your PC, for this example I used `MariaDB`.

> Firstly connect database by opening HeidiSQL application that comes with MariaDB.
Click `New`, then enter port number `3306`, database name `mariadb`, password `root`, and click `open`.
This will create connection to database named `mariadb`.
![Screenshot 2022-12-30 131412](https://user-images.githubusercontent.com/66387211/210064609-cc089c91-e14d-47de-9ac5-3ea8ce8ce76d.jpg)

> Now we can clone this repository `https://github.com/IKromans/Currency_rates.java.git`

> navigate to folder where `pom.xml` file is located, open terminal and run `mvn clean package`. This command will create `jar` files in `target` folder.

![Screenshot 2022-12-29 232128](https://user-images.githubusercontent.com/66387211/210015308-d96d8694-3100-4dd5-854f-80a387dde1eb.jpg)

> then you can use `cd ./target` command to go to `target` folder where jar files are located.

> run `java -jar CurrencyRatesApi-1.0-SNAPSHOT-jar-with-dependencies.jar` command and the program will prompt you to enter which command you want to execute - `fetch` or `activate`.

If you choose `fetch`:

![Screenshot 2022-12-29 234642](https://user-images.githubusercontent.com/66387211/210015506-6510e7d9-cf57-4cfe-9f73-63862f8179a6.jpg)

Program will get data and store them in created `mariadb` database in `exchange_rates` table when database is refreshed.

![Screenshot 2022-12-30 132330](https://user-images.githubusercontent.com/66387211/210065639-c79a1473-75cd-44be-bafd-66cfc347a1d8.jpg)

If you choose `activate`:

![Screenshot 2022-12-29 234548](https://user-images.githubusercontent.com/66387211/210015957-ac79b5e2-dfea-41ea-9768-b717e9643e5b.jpg)

Program will start endpoints:

> Getting latest currency rates from database - `http://localhost:7000/exchange-rates/latest`

![Screenshot 2022-12-30 001741](https://user-images.githubusercontent.com/66387211/210016328-96db7d19-ffd4-456b-9fa4-7773c4849a26.jpg)

> Getting previous currency rates for choosen currency from database - `http://localhost:7000/exchange-rates/{currency}`

![Screenshot 2022-12-30 001815](https://user-images.githubusercontent.com/66387211/210016338-d3d59ed5-b585-4748-aa5e-d0ca4732c80e.jpg)

Enjoy!
