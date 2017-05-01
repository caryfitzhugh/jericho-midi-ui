var Devices = {
  "JerichoMIDI/4": {
    button_types: {
      "0": "latching",
      "1": "momentary",
    },
    radio_groups: [1,2,3],
    commands: {
      request_config: "req-cfg",
      set_button: "set-btn",
    },
    responses: {
      request_config: "brd-cfg",
      button_config: "btn-cfg",
    },
    save_config: function (device) {
      var d =  Devices[device.name];
      device.config.buttons.forEach((button, i) => {
        MIDI.send_sys_ex("set-btn",
         [  /*button number*/ i,
            /*button_type*/ parseInt(invert(d.button_types)[button.type],10),
            /*radio_group*/ button.radio_group,
            /*on*/
            (button.on.midi_type & 0xF0 ) >> 4,
            (button.on.midi_type & 0x0F),
            button.on.note,
            button.on.volume,
            button.on.channel,
            /*off*/
            (button.off.midi_type & 0xF0 ) >> 4,
            (button.off.midi_type & 0x0F),
            button.off.note,
            button.off.volume,
            button.off.channel,
          ],
          device
        );
      });

    },
    process: function(msg, tokens, device) {
      var d =  Devices[device.name];

      device.config = device.config || {};

      if (msg == d.responses.request_config)  {
        // Config response:
        if (tokens[0] === "btns") {
          device.config.button_count = parseInt(tokens[1],10);
          device.config.buttons = [];
        }
      } else if  (msg == d.responses.button_config)  {
        if (tokens[0] === "i" &&
            tokens[2] === "inp_type" &&
            tokens[4] === "radio_group" &&
            tokens[6] === "on" &&
            tokens[12] === "off") {

          var indx = parseInt(tokens[1], 10);
          device.config.buttons[indx] = {
            type: d.button_types[tokens[3]],
            available_types: d.button_types,
            available_radio_groups: d.radio_groups,
            radio_group: parseInt(tokens[5],10),
            on: {
              // Double "byted"
              midi_type: (parseInt(tokens[7],10) << 4 ) | (parseInt(tokens[8]) & 0x0F),
              note: parseInt(tokens[9],10),
              volume: parseInt(tokens[10],10),
              channel: parseInt(tokens[11],10),
            },
            off: {
              // Double "byted"
              midi_type: (parseInt(tokens[13],10) << 4 ) | (parseInt(tokens[14]) & 0x0F),
              note: parseInt(tokens[15],10),
              volume: parseInt(tokens[16],10),
              channel: parseInt(tokens[17],10),
            }
          };
        } else {
          console.error("Unknown format", msg, tokens);
        }
      }
    }
  }
};
