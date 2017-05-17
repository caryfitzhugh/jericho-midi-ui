
var app = new Vue({
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
