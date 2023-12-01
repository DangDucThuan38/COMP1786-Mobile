import 'package:comp1786flutter2/db_helper.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import 'details_screen.dart';

class HomeScreen extends StatefulWidget {
  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  List<Map<String, dynamic>> _allData = [];
  List<Map<String, dynamic>> _searchedData = []; // Danh sách kết quả tìm kiếm
  bool _isLoading = true;
  final TextEditingController _searchController = TextEditingController(); // TextEditingController cho ô tìm kiếm

  void _refreshData() async {
    final data = await SQLHelper.getAllData();
    setState(() {
      _allData = data;
      _searchedData = data; // Ban đầu, danh sách tìm kiếm là toàn bộ danh sách cuộc đi bộ
      _isLoading = false;
    });
  }
  void _updateSearchResults(String query) {
    setState(() {
      _searchedData = _allData.where((data) {
        final name = data['name'].toLowerCase();
        return name.contains(query.toLowerCase());
      }).toList();
    });
  }

  @override
  void initState() {
    super.initState();
    _refreshData();
  }

  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _dateController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _lengthController = TextEditingController();
  final TextEditingController _levelController = TextEditingController();
  final TextEditingController _locationController = TextEditingController();
  final TextEditingController _destinationController = TextEditingController();
  final TextEditingController _parkController = TextEditingController();

  Future<void> _addData() async {
    await SQLHelper.createData(
        _nameController.text,
        _dateController.text,
        _descriptionController.text,
        _destinationController.text,
        _lengthController.text,
        _levelController.text,
        _locationController.text,
        _parkController.text);
    _refreshData();
  }


  Future<void> _showDeleteConfirmationDialog(int id) async {
    return showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text("Confirm Delete"),
          content: Text("Are you sure you want to delete this hike?"),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop(); // Đóng hộp thoại
              },
              child: Text("Cancel"),
            ),
            TextButton(
              onPressed: () {
                _deleteData(id); // Thực hiện xóa dữ liệu
                Navigator.of(context).pop(); // Đóng hộp thoại
              },
              child: Text("Delete"),
            ),
          ],
        );
      },
    );
  }

  Future<void> _updateData(int id) async {
    await SQLHelper.updateData(
        id,
        _nameController.text,
        _dateController.text,
        _descriptionController.text,
        _destinationController.text,
        _lengthController.text,
        _levelController.text,
        _locationController.text,
        _parkController.text);
    _refreshData();
  }

  void _deleteData(int id) async {
    await SQLHelper.deleteData(id);
    ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
      backgroundColor: Colors.red,
      content: Text("Hike Deleted"),
    ));
    _refreshData();
  }

  void showBottomSheet(int? id) async {
    if (id != null) {
      final existingData =
      _allData.firstWhere((element) => element['id'] == id);
      _nameController.text = existingData['name'];
      _dateController.text = existingData['date'];
      _descriptionController.text = existingData['description'];
      _destinationController.text = existingData['destination'];
      _lengthController.text = existingData['length'];
      _levelController.text = existingData['level'];
      _locationController.text = existingData['location'];
      _parkController.text = existingData['park'];
    }
    showModalBottomSheet(
      elevation: 5,
      isScrollControlled: true,
      context: context,
      builder: (_) => Container(
        padding: EdgeInsets.only(
          top: 30,
          left: 15,
          right: 15,
          bottom: MediaQuery.of(context).viewInsets.bottom + 50,
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.end,
          children: [
            TextFormField(
              controller: _nameController,
              validator: requiredField,
              autovalidateMode: AutovalidateMode.onUserInteraction,
              decoration: const InputDecoration(
                labelText: "Name Hike",
                hintText: "Name Hike",
                icon: Icon(Icons.person),
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 10),
            TextFormField(
                controller: _dateController,
                validator: requiredField,

                autovalidateMode: AutovalidateMode.onUserInteraction,
                // initialValue: DateTime.now() as String,
                decoration: const InputDecoration(

                    icon: Icon(Icons.calendar_today), //icon of text field
                    labelText: "Date of the hike " //label text of field
                ),
                readOnly: true,
                onTap: () async {
                  DateTime? pickedDate = await showDatePicker(
                      context: context,
                      initialDate: DateTime.now(),
                      firstDate: DateTime(
                          2022), //DateTime.now() - not to allow to choose before today.
                      lastDate: DateTime(2101));
                  if (pickedDate != null) {
                    String formattedDate =
                    DateFormat('dd-MM-yyyy').format(pickedDate);
                    _dateController.text = formattedDate;
                  }
                }),
            const SizedBox(
              height: 10.0,
            ),
            TextFormField(
              controller: _descriptionController,
              validator: requiredField,

              autovalidateMode: AutovalidateMode.onUserInteraction,
              decoration: const InputDecoration(
                labelText: "Description Hike",
                hintText: "Description Hike",
                icon: Icon(Icons.description),
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 10),
            TextFormField(
              controller: _destinationController,
              validator: requiredField,

              autovalidateMode: AutovalidateMode.onUserInteraction,
              decoration: const InputDecoration(
                labelText: "Destination",
                hintText: "Destination",
                icon: Icon(Icons.place),
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 10),
            TextFormField(
              controller: _lengthController,
              validator: requiredField,

              autovalidateMode: AutovalidateMode.onUserInteraction,
              decoration: const InputDecoration(
                labelText: "Length Hike",
                hintText: "Length Hike",
                icon: Icon(Icons.accessible_forward_outlined),
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 10),
            TextFormField(
              controller: _levelController,
              validator: requiredField,

              autovalidateMode: AutovalidateMode.onUserInteraction,
              decoration: const InputDecoration(
                labelText: "Level Hike",
                hintText: "Level",
                icon: Icon(Icons.leaderboard),
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 10),
            TextFormField(
              controller: _locationController,
              validator: requiredField,
              autovalidateMode: AutovalidateMode.onUserInteraction,
              decoration: const InputDecoration(
                labelText: "Location Hike",
                hintText: "Location Hike",
                icon: Icon(Icons.place),
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 10),
            Column(
              children: [
                ListTile(
                  leading: Icon(Icons.park),
                  title: Text("Park Hike"),
                ),
                RadioListTile<String>(
                  title: Text("Not Required"),
                  value: "Not Required",
                  groupValue: _parkController.text,
                  onChanged: (value) {
                    setState(() {
                      _parkController.text = value!;
                    });
                  },
                ),
                RadioListTile<String>(
                  title: Text("Required"),
                  value: "Required",
                  groupValue: _parkController.text,
                  onChanged: (value) {
                    setState(() {
                      _parkController.text = value!;
                    });
                  },
                ),
              ],
            ),
            SizedBox(height: 10),
            Center(
              child: ElevatedButton(
                onPressed: () async {
                  if (id == null) {
                    await _addData();
                  }
                  if (id != null) {
                    await _updateData(id);
                  }
                  _nameController.text = "";
                  _dateController.text = "";
                  _lengthController.text = "";
                  _descriptionController.text = "";
                  _levelController.text = "";
                  _locationController.text = "";
                  _destinationController.text = "";
                  _parkController.text = "";
                  Navigator.of(context).pop();
                  print("Data Added");
                },
                child: Padding(
                  padding: EdgeInsets.all(18),
                  child: Text(
                    id == null ? "Add Data" : "Edit Data",
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: const Text("Hiker Management App"),
        actions: [
          // Ô tìm kiếm
          Expanded(
            child: TextField(
              controller: _searchController,
              decoration: InputDecoration(
                hintText: 'Search hikes',
                icon: Icon(
                  Icons.search,
                  color: Colors.indigo,
                )
              ),
              onChanged: (query) {
                _updateSearchResults(query);
              },
            ),
          ),
        ],
      ),
      body: _isLoading
          ? Center(
        child: CircularProgressIndicator(),
      )
          : ListView.builder(
        itemCount: _searchedData.length,
        itemBuilder: (context, index) => Card(
          margin: EdgeInsets.all(15),
          child: InkWell(
            onTap: () {
              Navigator.of(context).push(
                MaterialPageRoute(
                  builder: (context) => HikeDetailsScreen(_searchedData[index]),
                ),
              );
            },
            child: ListTile(
              title: Padding(
                padding: EdgeInsets.symmetric(vertical: 5),
                child: Text(
                  _searchedData[index]['name'],
                  style: TextStyle(
                    fontSize: 20,
                  ),
                ),
              ),
              subtitle: Text(_searchedData[index]['date']),
              trailing: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  IconButton(
                    onPressed: () {
                      showBottomSheet(_searchedData[index]['id']);
                    },
                    icon: Icon(
                      Icons.edit,
                      color: Colors.indigo,
                    ),
                  ),
                  IconButton(
                    onPressed: () {
                      _showDeleteConfirmationDialog(_searchedData[index]['id']);
                    },
                    icon: Icon(
                      Icons.delete,
                      color: Colors.indigo,
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => showBottomSheet(null),
        child: Icon(Icons.add),
      ),
    );
  }

  String? requiredField(String? value) {
    if (value == null || value.isEmpty || value == "") {
      return "This field is not allowed to be empty";
    }
    return null;
  }
}
