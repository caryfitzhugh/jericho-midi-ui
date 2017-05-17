const ManufacturerName = "JerichoMIDI";
var MIDI = {
  midi_listener: (device) => {
    var dev_conf = Devices[device.name];

    device.input.addEventListener( 'midimessage', function(ev){

      if (ev.data[0] == 0xF0 && ev.data[ev.data.length-1] == 0xF7) {
        // msg-type:<datablob>
        var payload = String.fromCharCode.apply(String,ev.data.slice(1,-1)).split(":",2);
        var msg = payload[0];
        var data = payload[1];
        var tokens = data.split(",");
        if (tokens[tokens.length-1] === "end") {
          dev_conf.process(msg, tokens.slice(0,-1), device);
          app.update("midi_devices");
        }
      }});
  },
  save_device_config: (device) => {
    var dev_conf = Devices[device.name];
    dev_conf.save_config(device);
  },
  send_get_config: (device) => {
    var str = Devices[device.name].commands.request_config;
    console.log("Sending configuration request to: ", device.name);
    MIDI.send_sys_ex(str, [], device);
  },
  send_sys_ex: (str, data, device) => {
    // msg-type:<datablob>
    var bytes = [];
    var charCode;

    for (var i = 0; i < str.length; ++i)
    {
      charCode = str.charCodeAt(i);
      bytes.push(charCode & 0xFF);
    }

    bytes.unshift(0xF0);
    bytes.push(":".charCodeAt(0));
    bytes = bytes.concat(data);
    bytes.push(0xF7);

    if (bytes.length > 30) {
      console.error("Current Implementation limits sent messages to 30 bytes!");
    } else {
      // Trigger a systex message
      //  30 bytes max payload size DOWN
      device.output.send(bytes);
    }
  },

  load_devices: function (cb) {
    navigator.requestMIDIAccess({sysex: true})
      .then((res) => {
        var devices = {};

        res.onstatechange = function() {
          console.log("MIDI Changed... - Alert the user if their device is gone");
        }

        for (const input of res.inputs.values()) {
          if (input.name.match(ManufacturerName)) {
            if (!devices[input.name]) {
              devices[input.name] = [];
            }
            devices[input.name].push({name: input.name, input: input, output: null});
          }
        }

        for (const output of res.outputs.values()) {
          if (output.name.match(ManufacturerName)) {
            if (!devices[output.name]) {
              devices[output.name] = [{name: output.name, input: null, output:null}];
            }
            // Find first output which is null, and set it
            var device = devices[output.name].find(function (device) {
              return device.output === null;
            });

            if (device) {
              device.output = output;
            } else {
              // HOW??
            }
          }
        }

        // Clean up -- only things with 2 input / outputs
        var devices_list = [];
        Object.keys(devices).forEach((key) => {
          devices[key].forEach((val) => {
            // Attach Listeners to all the devices
            MIDI.midi_listener(val);
            devices_list.push(val);
          });
        });
        cb(devices_list);
    });
  }
};
