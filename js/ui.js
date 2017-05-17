var app = new Ractive({
  // The `el` option can be a node, an ID, or a CSS selector.
  el: '#app',

  // We could pass in a string, but for the sake of convenience
  // we're passing the ID of the <script> tag above.
  template: '#app_template',

  // Here, we're passing in some initial data
  data: {
    midi_devices: null,
    control_type: function (note) {
      return app.get("options.midi_control_types").find((el) => {
          return el.value === note;
        });
    },
    hex_display: function (v) {
      return "0x"+v.toString(16);
    },
    options: {
      midi_notes: (function() {
        var notes = [];
          for (var i = 0 ; i < 127; i++) {
            var name = {0: "C",
                        1: "C#",
                        2: "D",
                        3: "D#",
                        4: "E",
                        5: "F",
                        6: "F#",
                        7: "G",
                        8: "G#",
                        9: "A",
                        10:"A#",
                        11:"B"}[i % 12] + "" + (Math.floor(i / 12) - 1);
            notes.push({value: i, name: name});
          }
        return notes;
        })(),
      midi_control_types: [
       {value: 0x00, name: "Continuous controller #0", velocity: true},
       {value: 0x01, name: "Modulation wheel", velocity: true},
       {value: 0x02, name: "Breath control", velocity: true},
       {value: 0x03, name: "Continuous controller #3", velocity: true},
       {value: 0x04, name: "Foot controller", velocity: true},
       {value: 0x05, name: "Portamento time", velocity: true},
       {value: 0x06, name: "Data Entry", velocity: true},
       {value: 0x07, name: "Main Volume", velocity: true},
       {value: 0x08, name: "Continuous controller #8", velocity: true},
       {value: 0x09, name: "Continuous controller #9", velocity: true},
       {value: 0x0A, name: "Continuous controller #10", velocity: true},
       {value: 0x0B, name: "Continuous controller #11", velocity: true},
       {value: 0x0C, name: "Continuous controller #12", velocity: true},
       {value: 0x0D, name: "Continuous controller #13", velocity: true},
       {value: 0x0E, name: "Continuous controller #14", velocity: true},
       {value: 0x0F, name: "Continuous controller #15", velocity: true},
       {value: 0x10, name: "Continuous controller #16", velocity: true},
       {value: 0x11, name: "Continuous controller #17", velocity: true},
       {value: 0x12, name: "Continuous controller #18", velocity: true},
       {value: 0x13, name: "Continuous controller #19", velocity: true},
       {value: 0x14, name: "Continuous controller #20", velocity: true},
       {value: 0x15, name: "Continuous controller #21", velocity: true},
       {value: 0x16, name: "Continuous controller #22", velocity: true},
       {value: 0x17, name: "Continuous controller #23", velocity: true},
       {value: 0x18, name: "Continuous controller #24", velocity: true},
       {value: 0x19, name: "Continuous controller #25", velocity: true},
       {value: 0x1A, name: "Continuous controller #26", velocity: true},
       {value: 0x1B, name: "Continuous controller #27", velocity: true},
       {value: 0x1C, name: "Continuous controller #28", velocity: true},
       {value: 0x1D, name: "Continuous controller #29", velocity: true},
       {value: 0x1E, name: "Continuous controller #30", velocity: true},
       {value: 0x1F, name: "Continuous controller #31", velocity: true},
       {value: 0x20, name: "Continuous controller #0 ", velocity: true},
       {value: 0x21, name: "Modulation wheel", velocity: true},
       {value: 0x22, name: "Breath control", velocity: true},
       {value: 0x23, name: "Continuous controller #3", velocity: true},
       {value: 0x24, name: "Foot controller", velocity: true},
       {value: 0x25, name: "Portamento time", velocity: true},
       {value: 0x26, name: "Data entry", velocity: true},
       {value: 0x27, name: "Main volume", velocity: true},
       {value: 0x28, name: "Continuous controller #8 ", velocity: true},
       {value: 0x29, name: "Continuous controller #9 ", velocity: true},
       {value: 0x2A, name: "Continuous controller #10", velocity: true},
       {value: 0x2B, name: "Continuous controller #11", velocity: true},
       {value: 0x2C, name: "Continuous controller #12", velocity: true},
       {value: 0x2D, name: "Continuous controller #13", velocity: true},
       {value: 0x2E, name: "Continuous controller #14", velocity: true},
       {value: 0x2F, name: "Continuous controller #15", velocity: true},
       {value: 0x30, name: "Continuous controller #16", velocity: true},
       {value: 0x31, name: "Continuous controller #17", velocity: true},
       {value: 0x32, name: "Continuous controller #18", velocity: true},
       {value: 0x33, name: "Continuous controller #19", velocity: true},
       {value: 0x34, name: "Continuous controller #20", velocity: true},
       {value: 0x35, name: "Continuous controller #21", velocity: true},
       {value: 0x36, name: "Continuous controller #22", velocity: true},
       {value: 0x37, name: "Continuous controller #23", velocity: true},
       {value: 0x38, name: "Continuous controller #24", velocity: true},
       {value: 0x39, name: "Continuous controller #25", velocity: true},
       {value: 0x3A, name: "Continuous controller #26", velocity: true},
       {value: 0x3B, name: "Continuous controller #27", velocity: true},
       {value: 0x3C, name: "Continuous controller #28", velocity: true},
       {value: 0x3D, name: "Continuous controller #29", velocity: true},
       {value: 0x3E, name: "Continuous controller #30", velocity: true},
       {value: 0x3F, name: "Continuous controller #31", velocity: true},
       {value: 0x40, name: "Damper pedal on/off (Sustain)", onoff: true},
       {value: 0x41, name: "Portamento on/off", onoff: true},
       {value: 0x42, name: "Sustenuto on/off ", onoff: true},
       {value: 0x43, name: "Soft pedal on/off", onoff: true},
       {value: 0x44, name: "Undefined on/off ", onoff: true},
       {value: 0x45, name: "Undefined on/off ", onoff: true},
       {value: 0x46, name: "Undefined on/off ", onoff: true},
       {value: 0x47, name: "Undefined on/off ", onoff: true},
       {value: 0x48, name: "Undefined on/off ", onoff: true},
       {value: 0x49, name: "Undefined on/off ", onoff: true},
       {value: 0x4A, name: "Undefined on/off ", onoff: true},
       {value: 0x4B, name: "Undefined on/off ", onoff: true},
       {value: 0x4C, name: "Undefined on/off ", onoff: true},
       {value: 0x4D, name: "Undefined on/off ", onoff: true},
       {value: 0x4E, name: "Undefined on/off ", onoff: true},
       {value: 0x4F, name: "Undefined on/off ", onoff: true},
       {value: 0x50, name: "Undefined on/off ", onoff: true},
       {value: 0x51, name: "Undefined on/off ", onoff: true},
       {value: 0x52, name: "Undefined on/off ", onoff: true},
       {value: 0x53, name: "Undefined on/off ", onoff: true},
       {value: 0x54, name: "Undefined on/off ", onoff: true},
       {value: 0x55, name: "Undefined on/off ", onoff: true},
       {value: 0x56, name: "Undefined on/off ", onoff: true},
       {value: 0x57, name: "Undefined on/off ", onoff: true},
       {value: 0x58, name: "Undefined on/off ", onoff: true},
       {value: 0x59, name: "Undefined on/off ", onoff: true},
       {value: 0x5A, name: "Undefined on/off ", onoff: true},
       {value: 0x5B, name: "Undefined on/off ", onoff: true},
       {value: 0x5C, name: "Undefined on/off ", onoff: true},
       {value: 0x5D, name: "Undefined on/off ", onoff: true},
       {value: 0x5E, name: "Undefined on/off ", onoff: true},
       {value: 0x5F, name: "Undefined on/off ", onoff: true},
       {value: 0x60, name: "Data entry ", raw: true},
       {value: 0x61, name: "Data entry ", raw: true},
       {value: 0x62, name: "Undefined  ", raw: true},
       {value: 0x63, name: "Undefined  ", raw: true},
       {value: 0x64, name: "Undefined  ", raw: true},
       {value: 0x65, name: "Undefined  ", raw: true},
       {value: 0x66, name: "Undefined  ", raw: true},
       {value: 0x67, name: "Undefined  ", raw: true},
       {value: 0x67, name: "Undefined  ", raw: true},
       {value: 0x67, name: "Undefined  ", raw: true},
       {value: 0x67, name: "Undefined  ", raw: true},
       {value: 0x67, name: "Undefined  ", raw: true},
       {value: 0x68, name: "Undefined  ", raw: true},
       {value: 0x69, name: "Undefined  ", raw: true},
       {value: 0x6A, name: "Undefined  ", raw: true},
       {value: 0x6B, name: "Undefined  ", raw: true},
       {value: 0x6C, name: "Undefined  ", raw: true},
       {value: 0x6D, name: "Undefined  ", raw: true},
       {value: 0x6E, name: "Undefined  ", raw: true},
       {value: 0x6F, name: "Undefined  ", raw: true},
       {value: 0x70, name: "Undefined  ", raw: true},
       {value: 0x71, name: "Undefined  ", raw: true},
       {value: 0x72, name: "Undefined  ", raw: true},
       {value: 0x73, name: "Undefined  ", raw: true},
       {value: 0x74, name: "Undefined  ", raw: true},
       {value: 0x75, name: "Undefined  ", raw: true},
       {value: 0x76, name: "Undefined  ", raw: true},
       {value: 0x77, name: "Undefined  ", raw: true},
       {value: 0x78, name: "Undefined  ", raw: true},
       {value: 0x79, name: "Undefined  ", raw: true},
       {value: 0x7A, name: "Local control ", onoff: true},
      ].sort( function(a, b){
            var nameA=a.name.toLowerCase(), nameB=b.name.toLowerCase();
            if (nameA < nameB) //sort string ascending
              return -1;
            if (nameA > nameB)
              return 1;
            return 0; //default return value (no sorting)
            }),
      midi_channels: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],
      midi_types: [
        /*128*/{value: 0x80, name: "Note off"},//   Note Number (0-127)    Note Velocity (0-127)
        /*144*/{value: 0x90, name: "Note on"},
        /*160*/{value: 0xA0, name: "Polyphonic aftertouch"},
        /*176*/{value: 0xB0, name: "Control mode change"},
        /*192*/{value: 0xC0, name: "Program change"},
        /*208*/{value: 0xD0, name: "Channel aftertouch"},
        /*224*/{value: 0xE0, name: "Pitch wheel range"},
      ]
    }
  }
});

app.on('save_config', function(evt) {
  device = app.get('midi_devices.' + app.get("selected_midi_device"));
  if (device) {
    MIDI.save_device_config(device)
  }
});
app.on('reload_config', function(evt) {
  device = app.get('midi_devices.' + app.get("selected_midi_device"));
  if (device) {
    MIDI.send_get_config(device)
  }
});
app.on('load_devices', function(evt) {
  // A little hacky to load_devices this way - but app needs to be refreshed
  MIDI.load_devices((devices_list) => {app.set('midi_devices', devices_list);});
});

app.observe("selected_midi_device", function (indx) {
  device = app.get('midi_devices.' + indx);
  if (device) {
    MIDI.send_get_config(device)
  }
});

/*
aar app = new Vue({
  el: '#app',
  data: {
    midi_devices: [],
    selected_midi_device: null,
    midi_device_config: {},
    options: {
      //note_on Note Number (0-127)    Note Velocity (0-127)
      //note_off Note Number (0-127)    Note Velocity (0-127)
      //note aftertouch number, aftertouch
      //controlmode
      //programchange (program#)
      notes: [
        {value: 0, name: "C -1"},
        {value: 1, name: "C# -1"},
        {value: 2, name: "D -1"},
        {value: 3, name: "D# -1"},
        {value: 4, name: "E -1"},
        {value: 5, name: "F -1"},
        {value: 6, name: "F# -1"},
        {value: 7, name: "G -1"},
        {value: 8, name: "G# -1"},
        {value: 9,  name: "A -1"},
        {value: 10, name: "A# -1"},
        {value: 11, name: "B -1"},
        {value: 12, name: "C +0"},
        {value: 13, name: "C# +0"},
        {value: 14, name: "D +0"},
        {value: 15, name: "D# +0"},
        {value: 16, name: "E +0"},
        {value: 17, name: "F +0"},
        {value: 18, name: "F# +0"},
        {value: 19, name: "G +0"},
        {value: 20, name: "G# +0"},
        {value: 21, name: "A +0"},
        {value: 22, name: "A# +0"},
        {value: 23, name: "B +0"},
        {value: 24, name: "C +1"},
        {value: 25, name: "C# +1"},
        {value: 26, name: "D +1"},
        {value: 27, name: "D# +1"},
        {value: 28, name: "E +1"},
        {value: 29, name: "F +1"},
        {value: 30, name: "F# +1"},
        {value: 31, name: "G +1"},
        {value: 32, name: "G# +1"},
        {value: 33, name: "A +1"},
        {value: 34, name: "A# +1"},
        {value: 35, name: "B +1"},
        {value: 36, name: "C +2"},
        {value: 37, name: "C# +2"},
        {value: 38, name: "D +2"},
        {value: 39, name: "D# +2"},
        {value: 40, name: "E +2"},
        {value: 41, name: "F +2"},
        {value: 42, name: "F# +2"},
        {value: 43, name: "G +2"},
        {value: 44, name: "G# +2"},
        {value: 45, name: "A +2"},
        {value: 46, name: "A# +2"},
        {value: 47, name: "B +2"},
      ],
      midi_channels: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],
      midi_types: [
        {value: 0x80, name: "Note off"},//   Note Number (0-127)    Note Velocity (0-127)
        {value: 0x90, name: "Note on"},
        {value: 0xA0, name: "Polyphonic aftertouch"},
        {value: 0xB0, name: "Control mode change"},
        {value: 0xC0, name: "Program change"},
        {value: 0xD0, name: "Channel aftertouch"},
        {value: 0xE0, name: "Pitch wheel range"},
      ]
    }
  },
  watch: {
    selected_midi_device: function (val, oldVal) {
      MIDI.send_get_config(val);
    }
  },
  methods: {
    reload_config: function (evt) {
      MIDI.send_get_config(this.selected_midi_device);
    },
    save_current_config: function (evt) {
      MIDI.save_device_config(this.selected_midi_device);
    },
    load_midi: function (evt) {
      this.midi_devices = "loading"
      // A little hacky to load_devices this way - but app needs to be refreshed
      MIDI.load_devices(app,(devices_list) => {this.midi_devices = devices_list;});
    }
  }
});

Vue.component('button-evt-edit', {
  props: ['btn_evt', 'options'],
  template: `
  <div>
    <label> MIDI Type </label>
  {{btn_evt.midi_type}}
    <select v-model.number="btn_evt.midi_type">
      <option v-for="option in options.midi_types" v-bind:value="option.value">
        {{option.name}}
      </option>
    </select>

    <div v-if="btn_evt.midi_type === 0x80">
      <label> MIDI Note</label>
      <select v-model.number="btn_evt.note">
        <option v-for="option in options.notes" v-bind:value="option.value">
          {{option.name}}
        </option>
      </select>
    </div>

    <div v-if="btn_evt.midi_type === 0x90">
      <label> MIDI Note</label>
      <select v-model.number="btn_evt.note">
        <option v-for="option in options.notes" v-bind:value="option.value">
          {{option.name}}
        </option>
      </select>
      <input type='range' min='0' max='127' step='1'  v-model.number="btn_evt.volume"/>
    </div>

    <div v-if="btn_evt.midi_type === 0xA0">
    </div>

    <div v-if="btn_evt.midi_type === 0xB0">
    </div>

    <div v-if="btn_evt.midi_type === 0xC0">
    </div>

    <div v-if="btn_evt.midi_type === 0xD0">
    </div>

    <div v-if="btn_evt.midi_type === 0xE0">
    </div>

    <label> MIDI Channel </label>
    <select v-model.number="btn_evt.channel">
      <option v-for="option in options.midi_channels">
        {{option}}
      </option>
    </select>
  </div>
  `
});

Vue.component('button-edit', {
  props: ['index', 'button', 'options'],
  template: `
              <fieldset>
                <legend> Button {{index}} </legend>
                <label> Type </label>
                <select v-model="button.type">
                  <option v-for="available_type in button.available_types">{{available_type}}</option>
                </select>

                <label> Radio Group </label>

                <select v-model.number="button.radio_group">
                  <option value='0'>Off</option>
                  <option v-for="available_radio_group in button.available_radio_groups">{{available_radio_group}}</option>
                </select>

                <fieldset>
                  <legend> On </legend>
                  <button-evt-edit :btn_evt.sync="button.on" :options="options"></button-evt-edit>
                </fieldset>

                <fieldset>
                  <legend> Off </legend>
                  <button-evt-edit :btn_evt.sync="button.off" :options="options"></button-evt-edit>
                </fieldset>
              </fieldset>
  `
})
*/
