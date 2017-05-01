
var app = new Vue({
  el: '#app',
  data: {
    midi_devices: null,
    selected_midi_device: null,
    midi_device_config: {},
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
})
