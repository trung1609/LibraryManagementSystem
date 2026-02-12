import { RadioButtonChecked, RadioButtonUnchecked } from "@mui/icons-material";
import {
  FormControl,
  FormControlLabel,
  FormLabel,
  Radio,
  RadioGroup,
} from "@mui/material";
import React from "react";

const GenreFilter = ({ genres, selectedGenreId, onGenreSelect }) => {
  return (
    <div className="bg-white rounded-xl shadow-md p-4 border border-gray-100">
      <div className="flex items-center justify-between mb-4 pb-3 border-b border-gray-200">
        <h3 className="text-lg font-bold text-gray-900">Genres</h3>
        {selectedGenreId && (
          <button
            onClick={() => onGenreSelect(null)}
            className="text-sm text-indigo-600 hover:text-indigo-700 font-medium transition-colors"
          >
            Clear
          </button>
        )}
      </div>

      <div
        className={`flex items-center space-x-2 py-2 px-3 mb-2 rounded-lg cursor-pointer transition-all duration-200 
            ${!selectedGenreId ? "bg-indigo-50 text-indigo-700 font-semibold" : "text-gray-700 hover:bg-gray-50"}`}
        onClick={() => onGenreSelect(null)}
      >
        {!selectedGenreId ? (
          <RadioButtonChecked sx={{ fontSize: 16, color: "#4F46E5" }} />
        ) : (
          <RadioButtonUnchecked sx={{ fontSize: 16 }} />
        )}
        <span className="text-sm">All Genres</span>
      </div>

      <div className="space-y-1 pl-9 max-h-96 overflow-y-auto custom-scrollbar">
        <FormControl>
          <RadioGroup
            aria-labelledby="demo-radio-buttons-group-label"
            defaultValue="female"
            name="radio-buttons-group"
            onChange={onGenreSelect}
          >
            {genres.map((genre) => (
              <FormControlLabel
                key={genre.id}
                value={genre.id}
                control={<Radio />}
                label={genre.name}
              />
            ))}
          </RadioGroup>
        </FormControl>
      </div>
    </div>
  );
};

export default GenreFilter;
