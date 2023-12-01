import 'package:flutter/material.dart';

class HikeDetailsScreen extends StatelessWidget {
  final Map<String, dynamic> hikeData;

  HikeDetailsScreen(this.hikeData);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Hike Details'),
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Text(
              hikeData['name'],
              style: const TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
              textAlign: TextAlign.center,
            ),
            SizedBox(height: 10),
            Text(
              'Date: ${hikeData['date']}',
              style: const TextStyle(fontSize: 18),
            ),
            SizedBox(height: 10),
            Text(
              'Description: ${hikeData['description']}',
              style: const TextStyle(fontSize: 18),
            ),
            SizedBox(height: 10),
            Text(
              'Location: ${hikeData['location']}',
              style: const TextStyle(fontSize: 18),
            ),
            SizedBox(height: 10),
            Text(
              'Length: ${hikeData['length']}',
              style: const TextStyle(fontSize: 18),
            ),
            SizedBox(height: 10),
            Text(
              'Level: ${hikeData['level']}',
              style: const TextStyle(fontSize: 18),
            ),
            SizedBox(height: 10),
            Text(
              'Destination: ${hikeData['destination']}',
              style: const TextStyle(fontSize: 18),
            ),
            SizedBox(height: 10),
            Text(
              'Park: ${hikeData['park']}',
              style: const TextStyle(fontSize: 18),
            ),
          ],
        ),
      ),
    );
  }
}
